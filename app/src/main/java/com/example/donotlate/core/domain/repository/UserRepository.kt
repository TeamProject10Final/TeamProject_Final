package com.example.donotlate.core.domain.repository

import com.example.donotlate.core.domain.model.User

interface UserRepository {
    suspend fun getUserById(userId: String): Result<User>
    suspend fun getUserLocation(user: String): Result<User>
}