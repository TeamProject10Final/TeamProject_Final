package com.example.donotlate.core.domain.repository


import com.example.donotlate.core.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserById(userId: String): Flow<UserEntity>
    suspend fun getAllUsers(): Flow<List<UserEntity>>
}