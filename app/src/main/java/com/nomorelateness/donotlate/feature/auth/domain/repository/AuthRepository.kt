package com.nomorelateness.donotlate.feature.auth.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<String>
    suspend fun signUpWithKakao(name: String, email: String, password: String): Result<String>
    suspend fun logIn(email: String, password: String): Result<String>
    suspend fun getCurrentUser(): Flow<String>
    suspend fun sendVerification()
    suspend fun checkUserEmailVerification(): Boolean
    suspend fun deleteUser(): Result<String>
    suspend fun getCurrentUserWithKakao(uid: String): Flow<DocumentSnapshot?>
}