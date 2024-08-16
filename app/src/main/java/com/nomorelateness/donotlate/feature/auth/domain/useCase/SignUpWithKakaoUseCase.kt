package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository

class SignUpWithKakaoUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, uid: String): Result<String>{
        return authRepository.signUpWithKakao(name,email,uid)
    }
}