package com.example.donotlate.feature.auth.domain.useCase

import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class LogOutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() : Flow<Boolean> {
        return TODO()
    }
}