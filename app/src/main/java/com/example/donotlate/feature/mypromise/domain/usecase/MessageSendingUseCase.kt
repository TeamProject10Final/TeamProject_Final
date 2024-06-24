package com.example.donotlate.feature.mypromise.domain.usecase

import com.example.donotlate.core.domain.model.MessageEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MessageSendingUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    operator fun invoke(roomId: String, message: MessageEntity): Flow<Boolean> {
        return firebaseDataRepository.sendToMessage(roomId, message)
    }
}