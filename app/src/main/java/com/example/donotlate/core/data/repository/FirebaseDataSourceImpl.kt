package com.example.donotlate.core.data.repository


import android.util.Log
import com.example.donotlate.core.data.mapper.toEntity
import com.example.donotlate.core.data.mapper.toEntityList
import com.example.donotlate.core.data.mapper.toPromiseEntityList
import com.example.donotlate.core.data.mapper.toUserEntity
import com.example.donotlate.core.data.mapper.toUserEntityList
import com.example.donotlate.core.data.response.FriendRequestDTO
import com.example.donotlate.core.data.response.PromiseRoomResponse
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID


class FirebaseDataSourceImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FirebaseDataRepository {

    override suspend fun getUserDataById(userId: String): Flow<UserEntity> = flow {
        val documentSnapshot = db.collection("users").document(userId).get().await()
        val userResponse = documentSnapshot.toObject(UserResponse::class.java)
        if (userResponse != null) {
            emit(userResponse.toUserEntity())
        } else {
            throw NullPointerException("User not found or userResponse is null")
        }
    }.catch { e ->
        throw e
    }


    override suspend fun getAllUsers(): Flow<List<UserEntity>> = flow {
        val documentSnapshot = db.collection("users").get().await()
        val userResponse = documentSnapshot.toObjects(UserResponse::class.java)
        emit(userResponse.toUserEntityList())
    }

    override suspend fun makeAFriendRequest(
        toId: String,
        fromId: String,
        fromUserName: String,
        requestId: String
    ): Flow<Boolean> = flow {
        try {
            val request = hashMapOf(
                "toId" to toId,
                "fromId" to fromId,
                "status" to "request",
                "requestTime" to FieldValue.serverTimestamp(),
                "fromUserName" to fromUserName
            )
            db.collection("FriendRequests").document(requestId).set(request).await()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }


    override suspend fun getFriendsListFromFirebase(uid: String): Flow<List<UserEntity>> = flow {
        try {
            val document = db.collection("users").document(uid).get().await()
            val user = document.toObject(UserResponse::class.java)
            val friends = user?.friends ?: emptyList()
            val friendsList = friends.mapNotNull { friendId ->
                val friendDoc = db.collection("users").document(friendId).get().await()
                friendDoc.toObject(UserResponse::class.java)
            }
            Log.d("FirebaseRepositoryImpl", "Fetched Friends List: $friendsList")
            emit(friendsList.toUserEntityList())
        } catch (e: Exception) {
            Log.e("FirebaseRepositoryImpl", "Error fetching friends list", e)
            emit(emptyList())
        }
    }

    override suspend fun acceptFriendRequest(requestId: String): Flow<Boolean> = flow {
        try {
            val requestRef = db.collection("FriendRequests").document(requestId)
            Log.d(
                "accept Info",
                "Checking document for requestId: $requestId at path: ${requestRef.path}"
            )

            db.runTransaction { transaction ->
                val requestDoc = transaction.get(requestRef)

                if (requestDoc.exists()) {
                    val fromId = requestDoc.getString("fromId")
                        ?: throw IllegalStateException("fromId is missing")
                    val toId = requestDoc.getString("toId")
                        ?: throw IllegalStateException("toId is missing")
                    Log.d("accept Info", "fromId: $fromId, toId: $toId")

                    transaction.update(requestRef, "status", "accept")

                    val fromUserRef = db.collection("users").document(fromId)
                    val toUserRef = db.collection("users").document(toId)

                    transaction.update(fromUserRef, "friends", FieldValue.arrayUnion(toId))
                    transaction.update(toUserRef, "friends", FieldValue.arrayUnion(fromId))
                } else {
                    Log.e("accept Info", "Document does not exist: $requestId")
                }
            }
        } catch (e: Exception) {
            Log.e("accept Info", "Error updating document: $requestId", e)
        }
    }

    override suspend fun searchUserById(searchId: String): Flow<List<UserEntity>> = flow {
        val documents = db.collection("users").whereEqualTo("name", searchId).get().await()

        val userList = mutableListOf<UserEntity>()
        for (document in documents) {

            val user = document.toObject(UserResponse::class.java)
            Log.d("FirebaseRepositoryImpl", "Fetched User: $user")
            userList.add(user.toUserEntity())
        }
        emit(userList)
    }

    override suspend fun getFriendRequestStatus(requestId: String): Flow<FriendRequestEntity?> =
        flow {
            val docRef = db.collection("FriendRequests").document(requestId)
            val document = docRef.get().await()
            if (document.exists()) {
                val friendRequest = document.toObject(FriendRequestDTO::class.java)
                if (friendRequest != null) {
                    emit(friendRequest.toEntity())
                } else {
                    emit(null)
                }
            }
        }

    override suspend fun getFriendRequestsList(toId: String): Flow<List<FriendRequestEntity>> =
        flow {
            val documents = db.collection("FriendRequests")
                .whereEqualTo("toId", toId)
                .whereEqualTo("status", "request")
                .get()
                .await()

            val requestList = documents.map {
                it.toObject(FriendRequestDTO::class.java)
            }
            emit(requestList.toEntityList())
        }

    override suspend fun makeAPromiseRoom(
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<String>

    ): Flow<Boolean> = flow {
        try {
            val roomData = hashMapOf(
                "roomTitle" to roomTitle,
                "roomCreatedAt" to Timestamp.now(),
                "promiseTime" to promiseTime,
                "promiseDate" to promiseDate,
                "destination" to destination,
                "destinationLat" to destinationLat,
                "destinationLng" to destinationLng,
                "penalty" to penalty,
                "participants" to participants
            )
            val roomId = UUID.randomUUID().toString()
            Log.d("makeAChatRoom3", "title: ${participants}")
             db.collection("PromiseRooms").document(roomId).set(roomData).await()
            Log.d("makeAChatRoom4", "title: ${participants}")
            emit(true)
        } catch (e: Exception) {
            Log.d("makeAChatRoom", "실패 이유: ${e.message}")
            emit(false)
        }
    }
}


