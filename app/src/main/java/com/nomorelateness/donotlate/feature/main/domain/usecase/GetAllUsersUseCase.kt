package com.nomorelateness.donotlate.feature.main.domain.usecase

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase (private val firebaseDataRepository: FirebaseDataRepository){
    suspend operator fun invoke(): Flow<List<UserEntity>> {
        return firebaseDataRepository.getAllUsers()
    }
}