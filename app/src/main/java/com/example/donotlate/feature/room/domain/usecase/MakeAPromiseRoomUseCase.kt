package com.example.donotlate.feature.room.domain.usecase

import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MakeAPromiseRoomUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(
        roomId: String,
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<String>
    ): Flow<Boolean> {
        return firebaseDataRepository.makeAPromiseRoom(
            roomId,
            roomTitle,
            promiseTime,
            promiseDate,
            destination,
            destinationLat,
            destinationLng,
            penalty,
            participants
        )
    }
}
