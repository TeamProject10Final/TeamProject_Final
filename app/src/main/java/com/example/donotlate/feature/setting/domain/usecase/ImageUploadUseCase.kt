package com.example.donotlate.feature.setting.domain.usecase

import android.net.Uri
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import com.example.donotlate.feature.setting.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.lang.IllegalStateException

class ImageUploadUseCase(private val settingRepository: SettingRepository){
//    suspend operator fun invoke(uri: Uri): Flow<Result<String>> = flow {
//        return try {
//            val uid = settingRepository.getCurrentUserID().first() ?: throw IllegalStateException("User not Login")
//            val imageUrl = settingRepository.uploadProfileImage(uid, uri).first()
//            val updateResult = settingRepository.updateProfileImage(uid, imageUrl)
//            if(updateResult){
//                emit(Result.success(imageUrl))
//            }
//        }catch (e:Exception){
//            Result.failure(e)
//        }
//    }
}
