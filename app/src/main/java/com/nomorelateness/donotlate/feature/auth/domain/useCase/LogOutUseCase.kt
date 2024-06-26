package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LogOutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() : Flow<Boolean> {
        return TODO()
    }
}