package com.example.donotlate.core.domain.repository


import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface FirebaseDataRepository {
    suspend fun getUserDataById(userId: String): Flow<UserEntity?>
    suspend fun getAllUsers(): Flow<List<UserEntity>>
    suspend fun makeAFriendRequest(toId: String, fromId: String, fromUserName: String, requestId: String) : Flow<Boolean>
    suspend fun getFriendsListFromFirebase(uid: String) : Flow<List<UserEntity>>
    suspend fun acceptFriendRequest(requestId: String) : Flow<Boolean>
    suspend fun searchUserById(searchId: String): Flow<List<UserEntity>>
    suspend fun getFriendRequestStatus(requestId: String): Flow<FriendRequestEntity?>
    suspend fun getFriendRequestsList(toId: String): Flow<List<FriendRequestEntity>>
}