package com.example.donotlate.feature.friends.domain.repository

import com.example.donotlate.feature.friends.data.repository.FriendRequestRepositoryImpl
import kotlinx.coroutines.flow.Flow

interface FriendRequestRepository{

    suspend fun makeAFriendRequest(toId: String, fromId: String, fromUserName: String) : Flow<Boolean>
}
