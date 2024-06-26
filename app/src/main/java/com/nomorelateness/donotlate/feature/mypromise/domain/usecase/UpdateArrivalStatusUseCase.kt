package com.nomorelateness.donotlate.feature.mypromise.domain.usecase

import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class UpdateArrivalStatusUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    operator fun invoke(roomId: String, uid: String): Flow<Boolean> {
        return firebaseDataRepository.updateArrivalStatus(roomId, uid)
    }
}