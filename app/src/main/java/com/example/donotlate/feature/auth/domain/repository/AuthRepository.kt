package com.example.donotlate.feature.auth.domain.repository

import com.example.donotlate.core.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(name:String, email: String, password: String): Result<String>
    suspend fun logIn(email: String, password: String): Result<String>
    suspend fun getCurrentUser(): Flow<String>
}