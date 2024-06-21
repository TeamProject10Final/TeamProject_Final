package com.example.donotlate.feature.friends.domain.repository

import kotlinx.coroutines.flow.Flow

interface FriendRequestRepository{

    suspend fun makeAFriendRequest(toId: String, fromId: String, fromUserName: String) : Flow<Boolean>
}
