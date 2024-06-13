package com.example.donotlate.feature.setting.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun uploadProfileImage(uid: String, uri: Uri): Flow<String>
    suspend fun updateProfileImage(uid:String, uri: String): Flow<Boolean>
    suspend fun getCurrentUserID(): Flow<String>
}