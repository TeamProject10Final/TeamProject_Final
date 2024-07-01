package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository

class SendEmailVerificationUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        return authRepository.sendVerification()
    }
}