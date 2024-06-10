package com.example.donotlate.feature.auth.domain.useCase

import com.example.donotlate.feature.auth.domain.repository.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<String>{
        return authRepository.signUp(name,email,password)
    }
}