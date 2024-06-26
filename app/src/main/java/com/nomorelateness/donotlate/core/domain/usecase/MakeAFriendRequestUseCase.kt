package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MakeAFriendRequestUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(toId: String, fromId: String, fromUserName: String, requestId: String): Flow<Boolean> {
        return firebaseDataRepository.makeAFriendRequest(toId, fromId, fromUserName, requestId)
    }
}