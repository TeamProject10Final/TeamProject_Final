package com.nomorelateness.donotlate.feature.auth.data.repository

import android.content.Context
import com.example.donotlate.feature.auth.data.model.RegisterUserDTO
import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    val context: Context
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            addUserToFireStore(name, email, user?.uid!!)
            Result.success("SignUp Success")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            return Result.success("LogIn Success")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Flow<String> = flow{
        try {
            val currentUserUId: String = auth.currentUser?.uid ?: ""
            emit(currentUserUId)

        } catch (e: Exception) {
            Result.failure<Exception>(e)

        }
    }


    private fun addUserToFireStore(name: String, email: String, uId: String) {
        val user = RegisterUserDTO(name, email, Firebase.auth.uid!!, createdAt = Timestamp.now())
        db.collection("users").document(uId)
            .set(user)
    }
}