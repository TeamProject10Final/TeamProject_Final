package com.nomorelateness.donotlate.core.domain.usecase.promiseusecase

import com.nomorelateness.donotlate.core.domain.repository.PromiseRoomRepository
import kotlinx.coroutines.flow.Flow

class RemoveParticipantsUseCase(private val promiseRoomRepository: PromiseRoomRepository) {
    operator fun invoke(roomId: String, participantId: String): Flow<Boolean> {
        return promiseRoomRepository.removeParticipant(roomId, participantId)
    }
}