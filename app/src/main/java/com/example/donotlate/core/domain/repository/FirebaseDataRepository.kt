package com.example.donotlate.core.domain.repository


import com.example.donotlate.core.data.response.MessageResponse
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.MessageEntity
import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface FirebaseDataRepository {
    suspend fun getUserDataById(userId: String): Flow<UserEntity?>
    suspend fun getAllUsers(): Flow<List<UserEntity>>
    suspend fun makeAFriendRequest(
        toId: String,
        fromId: String,
        fromUserName: String,
        requestId: String
    ): Flow<Boolean>

    suspend fun getFriendsListFromFirebase(uid: String): Flow<List<UserEntity>>
    suspend fun acceptFriendRequest(requestId: String): Flow<Boolean>
    suspend fun searchUserById(searchId: String): Flow<List<UserEntity>>
    suspend fun getFriendRequestStatus(requestId: String): Flow<FriendRequestEntity?>
    suspend fun getFriendRequestsList(toId: String): Flow<List<FriendRequestEntity>>
    suspend fun makeAPromiseRoom(
        roomId: String,
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<String>
    ): Flow<Boolean>

    suspend fun getMyPromiseListFromFireBase(uid: String): Flow<List<PromiseRoomEntity>>
    suspend fun getCurrentUserDataFromFireBase(): Flow<UserEntity?>
    suspend fun loadToMessage(roomId: String): Flow<List<MessageEntity>>
    suspend fun sendToMessage(roomId: String, message: MessageResponse): Flow<Boolean>
}