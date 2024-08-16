package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.UserEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserDataUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(uid: String): Flow<UserEntity?> {
        return firebaseDataRepository.getCurrentUserDataFromFireBase()
    }
}