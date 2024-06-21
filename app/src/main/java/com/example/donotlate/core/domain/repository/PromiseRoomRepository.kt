package com.example.donotlate.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface PromiseRoomRepository {
    suspend fun requestDeleteRoom(roomId:String): Flow<Boolean>
    suspend fun removeParticipant(roomId: String, participantId: String): Flow<Boolean>
}