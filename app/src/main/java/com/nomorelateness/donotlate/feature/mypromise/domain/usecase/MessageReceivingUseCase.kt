package com.nomorelateness.donotlate.feature.mypromise.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.MessageEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MessageReceivingUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(roomId: String): Flow<List<MessageEntity>> {
        return firebaseDataRepository.loadToMessage(roomId)
    }
}