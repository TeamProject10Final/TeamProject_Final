package com.example.donotlate.core.data.repository


import android.util.Log
import com.example.donotlate.core.data.mapper.toEntity
import com.example.donotlate.core.data.mapper.toMessageEntity
import com.example.donotlate.core.data.mapper.toPromiseEntityList
import com.example.donotlate.core.data.mapper.toUserEntity
import com.example.donotlate.core.data.mapper.toUserEntityList
import com.example.donotlate.core.data.response.FriendRequestDTO
import com.example.donotlate.core.data.response.MessageResponse
import com.example.donotlate.core.data.response.PromiseRoomResponse
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.MessageEntity
import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

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
            Log.d("requestId", "$requestId")
            val request = hashMapOf(
                "requestId" to requestId,
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

                    true
                } else {
                    Log.e("accept Info", "Document does not exist: $requestId")
                    false
                }
            }.await()
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
            // 친구 요청 목록 받는 코드
            // 일단 한 번 받아옴.
            val documents = db.collection("FriendRequests")
                .whereEqualTo("toId", toId)
                .whereEqualTo("status", "request")
                .get().await()

            val requestList = documents.documents.mapNotNull { document ->
                document.toObject(FriendRequestDTO::class.java)?.copy()?.toEntity()
            }
            emit(requestList)

            // 이후 callbackFlow를 활용하여 변경사항이 생기면 실시간으로 받아서 보냄
            // 해당 내용 쪼개서(?) 적용할 지 결정할 예정
            callbackFlow {
                val documents = db.collection("FriendRequests")
                    .whereEqualTo("toId", toId)
                    .whereEqualTo("status", "request")
                val listener = documents.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val updateRequestList = snapshot.documents.mapNotNull { updateDocument ->
                            updateDocument.toObject(FriendRequestDTO::class.java)?.copy()
                                ?.toEntity()
                        }
                        trySend(updateRequestList).isSuccess
                    }
                }
                awaitClose { listener.remove() }
                // 실시간 변동이 발생 시 컬렉트를 해서 다시 에밋해서 내보냄.
            }.collect {
                emit(it)
            }
        }

    override suspend fun makeAPromiseRoom(roomInfo: PromiseRoomEntity): Flow<Boolean> = flow {
        try {
            val roomId = roomInfo.roomId
            db.collection("PromiseRooms").document(roomId).set(roomInfo).await()

            // participants 리스트를 통해 isArrived 맵을 초기화
//            val updatedRoomInfo = roomInfo.copy(
//                isArrived = roomInfo.participants.associateWith { false }.toMutableMap()
//            )

//            roomRef.set(updatedRoomInfo).await()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override suspend fun getMyPromiseListFromFireBase(uid: String): Flow<List<PromiseRoomEntity>> =
        callbackFlow {
//            try {
            val query = db.collection("PromiseRooms")
                .whereArrayContains("participants", uid)
                .orderBy("promiseDate", Query.Direction.ASCENDING)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList()).isSuccess
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val promiseRoom = snapshot.toObjects(PromiseRoomResponse::class.java)
                    trySend(promiseRoom.toPromiseEntityList()).isSuccess
                }
            }
            awaitClose {
                listenerRegistration.remove()
            }

//                if (documents.isEmpty) {
//                    Log.d("getMyPromiseListFromFireBase", "No documents found")
//                } else {
//                    Log.d("getMyPromiseListFromFireBase", "Found ${documents.size()} documents")
//                }
//                val document = documents.toObjects(PromiseRoomResponse::class.java)
//                trySend(document.toPromiseEntityList())
//            } catch (e: Exception) {
//                Log.e("getMyPromiseListFromFireBase", "Error fetching promise list")
//                Log.e("getMyPromiseListFromFireBase", "Error fetching promise list", e)
//                trySend(emptyList())
//            }
        }

    override suspend fun getCurrentUserDataFromFireBase(): Flow<UserEntity?> = flow {
        val mAuth = auth.currentUser?.uid

        if (mAuth != null) {
            val documentSnapshot = db.collection("users").document(mAuth).get().await()
            val userResponse = documentSnapshot.toObject(UserResponse::class.java)
            if (userResponse != null) {
                emit(userResponse.toUserEntity())
            } else {
                return@flow
            }
        }
    }.catch { e ->
        throw e
    }

    override suspend fun loadToMessage(roomId: String): Flow<List<MessageEntity>> = callbackFlow {
        val documents = db.collection("PromiseRooms").document(roomId).collection("Messages")
            .orderBy("sendTimestamp")

        val listener = documents.addSnapshotListener { snapshots, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshots != null) {
                val messages = snapshots.documents.mapNotNull { document ->
                    document.toObject(MessageResponse::class.java)?.copy(messageId = document.id)
                        ?.toMessageEntity()
                }
                trySend(messages).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun sendToMessage(roomId: String, message: MessageEntity): Flow<Boolean> =
        flow {
            try {
                db.collection("PromiseRooms").document(roomId).collection("Messages")
                    .add(message)
                    .await()
                emit(true)
                Log.d("ddddddd7", "$roomId")
            } catch (e: Exception) {
                Log.d("SendToMessage", "Error: Send To Message Error: $e")
                emit(false)
            }
        }

    override suspend fun updateArrivalStatus(roomId: String, uid: String): Flow<Boolean> = flow {

        try {
            val roomRef = db.collection("PromiseRooms").document(roomId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(roomRef)
                val roomInfo = snapshot.toObject(PromiseRoomResponse::class.java)

                if (roomInfo != null) {
                    val isArrived = roomInfo.hasArrived.toMutableMap()
                    isArrived[uid] = true
                    transaction.update(roomRef, "hasArrived", isArrived)
                }
            }.await()

            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override suspend fun updatePromiseRoom() {
        TODO("Not yet implemented")
    }


    private suspend fun readRequest(requestId: String): DocumentSnapshot {
        val requestRef = db.collection("FriendRequests").document(requestId)
        Log.d("accept Info", "Checking document for requestId: $requestId")
        return requestRef.get().await()
    }

    private suspend fun updateFriendRequest(fromId: String, toId: String, requestId: String) {
        db.runTransaction { transaction ->
            val requestRef = db.collection("FriendRequests").document(requestId)
            val fromUserRef = db.collection("users").document(fromId)
            val toUserRef = db.collection("users").document(toId)

            transaction.update(requestRef, "status", "accept")
            transaction.update(fromUserRef, "friends", FieldValue.arrayUnion(toId))
            transaction.update(toUserRef, "friends", FieldValue.arrayUnion(fromId))
        }.await()
    }
}


