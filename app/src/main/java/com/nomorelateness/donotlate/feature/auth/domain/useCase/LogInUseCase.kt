package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LogInUseCase
@Inject
constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.logIn(email, password)
    }
}