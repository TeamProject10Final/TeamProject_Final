package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.FriendRequestEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class GetFriendRequestsStatusUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(requestId:String): Flow<FriendRequestEntity?> {
        return firebaseDataRepository.getFriendRequestStatus(requestId)
    }
}