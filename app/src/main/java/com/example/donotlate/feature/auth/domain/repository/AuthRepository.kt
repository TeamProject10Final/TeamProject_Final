package com.example.donotlate.feature.auth.domain.repository

interface AuthRepository {
    suspend fun signUp(name:String, email: String, password: String): Result<String>
    suspend fun logIn(email: String, password: String): Result<String>

}