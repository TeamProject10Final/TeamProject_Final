package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class AcceptFriendRequestsUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(requestId:String): Flow<Boolean> {
        return firebaseDataRepository.acceptFriendRequest(requestId)
    }
}