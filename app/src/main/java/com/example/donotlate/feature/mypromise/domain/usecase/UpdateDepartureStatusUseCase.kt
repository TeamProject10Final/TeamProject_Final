package com.example.donotlate.feature.mypromise.domain.usecase

import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class UpdateDepartureStatusUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    operator fun invoke(roomId: String, uid: String): Flow<Boolean> {
        return firebaseDataRepository.updateDepartureStatus(roomId, uid)
    }
}