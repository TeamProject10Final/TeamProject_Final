package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository

class LogInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.logIn(email, password)
    }
}