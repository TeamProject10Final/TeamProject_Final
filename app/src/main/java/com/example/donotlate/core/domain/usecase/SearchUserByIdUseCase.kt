package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class SearchUserByIdUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(searchId:String): Flow<List<UserEntity>> {
        return firebaseDataRepository.searchUserById(searchId)
    }
}