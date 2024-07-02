package com.nomorelateness.donotlate.core.domain.usecase.promiseusecase

import com.nomorelateness.donotlate.core.domain.repository.PromiseRoomRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class RemoveParticipantsUseCase
@Inject
constructor(private val promiseRoomRepository: PromiseRoomRepository) {
    operator fun invoke(roomId: String, participantId: String): Flow<Boolean> {
        return promiseRoomRepository.removeParticipant(roomId, participantId)
    }
}