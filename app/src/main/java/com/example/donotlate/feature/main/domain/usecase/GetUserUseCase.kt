package com.example.donotlate.feature.main.domain.usecase

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase (private val firebaseDataRepository: FirebaseDataRepository){
    suspend operator fun invoke(userId:String): Flow<UserEntity?> {
        return firebaseDataRepository.getUserDataById(userId)
    }
}