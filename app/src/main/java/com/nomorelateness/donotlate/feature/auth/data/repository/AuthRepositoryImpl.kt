package com.nomorelateness.donotlate.feature.auth.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.nomorelateness.donotlate.feature.auth.data.model.RegisterUserDTO
import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository
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
            if (user != null) {
                addUserToFireStore(name, email, user.uid)
                sendVerification()
                auth.signOut()
                Result.success("SignUp Success")
            } else {
                Result.failure(Exception("User creation failed"))
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception("FirebaseAuthException: ${e.message}"))
        } catch (e: FirebaseFirestoreException) {
            Result.failure(Exception("FirebaseFirestoreException: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Exception: ${e.message}"))
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

    override suspend fun getCurrentUser(): Flow<String> = flow {
        try {
            val currentUserUId: String = auth.currentUser?.uid ?: ""
            emit(currentUserUId)

        } catch (e: Exception) {
            Result.failure<Exception>(e)

        }
    }

    override suspend fun sendVerification() {
        try {
            val user = auth.currentUser
            Log.d("AuthRepositoryImpl", "${user}")
            if (user != null) {
                user.sendEmailVerification().await()
                Log.d("AuthRepositoryImpl", "Verification email sent")
            } else {
                Log.d("AuthRepositoryImpl", "No current user found")
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error sending verification email: ${e.message}")
        }
    }

    override suspend fun checkUserEmailVerification(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified == true
    }

    override suspend fun deleteUser(): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                db.collection("users").document(userId).delete().await()
                auth.currentUser?.delete()
                Result.success("Delete Success")
            } else {
                Result.failure(Exception("Delete Failure"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getCurrentUserWithKakao(uid: String): Flow<DocumentSnapshot?> = flow {
        try {
            val document = db.collection("users").document(uid).get().await()
            emit(document)
        } catch (e: Exception) {
            emit(null)
        }

    }

    override suspend fun signUpWithKakao(name: String, email: String, uId: String): Result<String> {
        return try {
            val user = RegisterUserDTO(name,email, uId, createdAt = Timestamp.now())
            db.collection("users").document(uId).set(user).await()
            Result.success("SignUp Success")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun addUserToFireStore(name: String, email: String, uId: String) {
        val user = RegisterUserDTO(name, email, Firebase.auth.uid!!, createdAt = Timestamp.now())
        db.collection("users").document(uId)
            .set(user)
    }
}