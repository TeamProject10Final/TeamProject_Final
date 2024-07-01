package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository

class DeleteUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<String> {
        return authRepository.deleteUser()
    }
}