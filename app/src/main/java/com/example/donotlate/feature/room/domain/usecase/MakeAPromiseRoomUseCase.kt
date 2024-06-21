package com.example.donotlate.feature.room.domain.usecase

import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MakeAPromiseRoomUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(roomInfo:PromiseRoomEntity): Flow<Boolean> {
        return firebaseDataRepository.makeAPromiseRoom(roomInfo)
    }
}
