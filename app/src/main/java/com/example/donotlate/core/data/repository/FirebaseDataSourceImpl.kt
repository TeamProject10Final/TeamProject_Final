package com.example.donotlate.core.data.repository


import android.util.Log
import com.example.donotlate.core.data.mapper.toEntity
import com.example.donotlate.core.data.mapper.toEntityList
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseDataSourceImpl(
    private val db: FirebaseFirestore,
) : UserRepository {

    override suspend fun getUserById(userId: String): Flow<UserEntity> = flow {
        val documentSnapshot = db.collection("users").document(userId).get().await()
        val userResponse = documentSnapshot.toObject(UserResponse::class.java)
        if (userResponse != null) {
            emit(userResponse.toEntity())
        } else {
            throw NullPointerException("User not found or userResponse is null")
        }
    }.catch { e ->
        throw e
    }


    override suspend fun getAllUsers(): Flow<List<UserEntity>> = flow {
        val documentSnapshot = db.collection("users").get().await()
        val userResponse = documentSnapshot.toObjects(UserResponse::class.java)
        emit(userResponse.toEntityList())
    }


}

