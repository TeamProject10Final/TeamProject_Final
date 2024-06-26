package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.PromiseRoomEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class LoadToMyPromiseListUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(uid: String): Flow<List<PromiseRoomEntity>> {
        return firebaseDataRepository.getMyPromiseListFromFireBase(uid)
    }
}