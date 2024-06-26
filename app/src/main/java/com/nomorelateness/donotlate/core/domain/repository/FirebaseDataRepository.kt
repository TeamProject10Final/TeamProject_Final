package com.nomorelateness.donotlate.core.domain.repository


import com.nomorelateness.donotlate.core.domain.model.FriendRequestEntity
import com.nomorelateness.donotlate.core.domain.model.MessageEntity
import com.nomorelateness.donotlate.core.domain.model.PromiseRoomEntity
import com.nomorelateness.donotlate.core.domain.model.UserEntity
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
    suspend fun makeAPromiseRoom(roomInfo: PromiseRoomEntity): Flow<Boolean>
    suspend fun getMyPromiseListFromFireBase(uid: String): Flow<List<PromiseRoomEntity>>
    suspend fun getCurrentUserDataFromFireBase(): Flow<UserEntity?>
    suspend fun loadToMessage(roomId: String): Flow<List<MessageEntity>>
//    suspend fun sendToMessage(roomId: String, message: MessageEntity): Flow<Boolean>
//    suspend fun updateArrivalStatus(roomId: String, uid: String): Flow<Boolean>
//    suspend fun updateDepartureStatus(roomId: String, uid: String): Flow<Boolean>
    fun sendToMessage(roomId: String, message: MessageEntity): Flow<Boolean>
    fun updateArrivalStatus(roomId: String, uid: String): Flow<Boolean>
    fun updateDepartureStatus(roomId: String, uid: String): Flow<Boolean>
    suspend fun updatePromiseRoom()
}

// 나가기, 방 삭제 두 개가 있어야 함
// 이유?? 내가 의도하지 않은 방일 수 도 있어서?
// 방 삭제 - 동의를 해야하면 누군가가 한 명이 반대할 경우 방이 계속 유지가 될 가능성이 있음
// 그래서 방삭제 또는 방나가기를 통해서 방을 삭제할 수 있어야 함.