package com.example.donotlate.core.domain.usecase

import android.util.Log
import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Flow<String> {
        return authRepository.getCurrentUser().onEach { uid ->
            Log.d("GetCurrentUserUseCase", "Current User UID: $uid")
        }
    }
}


