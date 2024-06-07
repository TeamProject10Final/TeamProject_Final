package com.example.donotlate.feature.main.domain.usecase

import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Flow<String> {
        return authRepository.getCurrentUser()
    }
}


