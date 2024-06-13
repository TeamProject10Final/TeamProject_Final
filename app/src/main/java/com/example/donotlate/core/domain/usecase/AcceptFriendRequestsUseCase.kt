package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class AcceptFriendRequestsUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(requestId:String): Flow<Boolean> {
        return firebaseDataRepository.acceptFriendRequest(requestId)
    }
}