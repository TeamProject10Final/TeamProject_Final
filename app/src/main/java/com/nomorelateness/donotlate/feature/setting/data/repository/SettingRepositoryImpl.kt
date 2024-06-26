package com.nomorelateness.donotlate.feature.setting.data.repository

import android.net.Uri
import com.example.donotlate.feature.setting.domain.repository.SettingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SettingRepositoryImpl(private val auth: FirebaseAuth, private val db: FirebaseFirestore, private val storage: FirebaseStorage):SettingRepository {

    override suspend fun uploadProfileImage(uid: String, uri: Uri): Flow<String> = flow {
        val fileName = UUID.randomUUID().toString()
        val profileImagesRef = storage.reference.child("Images/").child(uid).child(fileName)
        val uploadTask = profileImagesRef.putFile(uri).await()
        val downloadUrl = profileImagesRef.downloadUrl.await().toString()
        emit(downloadUrl)
    }

    override suspend fun updateProfileImage(uid: String, uri: String): Flow<Boolean> = flow{
        val userRef = db.collection("users").document(uid)
        userRef.update("profileImgUrl", uri).await()
        emit(true)
    }

    override suspend fun getCurrentUserID(): Flow<String> = flow {
        val uid = auth.currentUser?.uid ?: throw IllegalStateException("User Not Login")
        emit(uid)
    }
}