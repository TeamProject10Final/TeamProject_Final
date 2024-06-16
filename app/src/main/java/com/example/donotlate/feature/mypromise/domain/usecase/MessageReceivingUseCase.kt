package com.example.donotlate.feature.mypromise.domain.usecase

import com.example.donotlate.core.domain.model.MessageEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MessageReceivingUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(roomId: String): Flow<List<MessageEntity>> {
        return firebaseDataRepository.loadToMessage(roomId)
    }
}