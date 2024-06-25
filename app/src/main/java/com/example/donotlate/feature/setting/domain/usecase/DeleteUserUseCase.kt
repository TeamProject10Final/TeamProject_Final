package com.example.donotlate.feature.setting.domain.usecase

import com.example.donotlate.core.domain.repository.UserRepository
import com.example.donotlate.feature.main.presentation.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): Flow<Result<Unit>> = flow{
        try {
            userRepository.deleteUserData(userId)
            userRepository.deleteUserAccount()
            emit(Result.Success(Unit))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }
}