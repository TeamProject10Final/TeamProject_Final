package com.example.donotlate.feature.main.domain.usecase

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase (private val userRepository: UserRepository){
    suspend operator fun invoke(): Flow<List<UserEntity>> {
        return userRepository.getAllUsers()
    }
}