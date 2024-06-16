package com.example.donotlate.feature.mypromise.domain.usecase

import android.util.Log
import com.example.donotlate.core.data.mapper.toMessageResponse
import com.example.donotlate.core.domain.model.MessageEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class MessageSendingUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(roomId: String, message: MessageEntity): Flow<Boolean> {
        Log.d("ddddddd5", "$roomId")
        return firebaseDataRepository.sendToMessage(roomId, message.toMessageResponse())
        Log.d("ddddddd6", "$roomId")
    }
}