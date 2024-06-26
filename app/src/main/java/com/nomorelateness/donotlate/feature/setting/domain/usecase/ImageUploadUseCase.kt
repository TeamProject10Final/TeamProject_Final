package com.nomorelateness.donotlate.feature.setting.domain.usecase

import android.net.Uri
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.feature.setting.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ImageUploadUseCase(private val settingRepository: SettingRepository) {
    suspend operator fun invoke(uri: Uri): Flow<Result<String>> = flow {
        try {
            val mAuth = CurrentUser.userData?.uId
            if (mAuth != null) {
                val imageUrl = settingRepository.uploadProfileImage(mAuth, uri).first()
                val updateResult = settingRepository.updateProfileImage(mAuth, imageUrl)
                if (updateResult.first()) {
                    emit(Result.success(imageUrl))
                } else {
                    emit(Result.failure(Exception("Failed to update profile image Url")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
