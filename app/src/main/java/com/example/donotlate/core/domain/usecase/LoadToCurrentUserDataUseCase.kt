package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow


class LoadToCurrentUserDataUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(): Flow<UserEntity?> {
        return firebaseDataRepository.getMyDataFromFireStore()
    }
}