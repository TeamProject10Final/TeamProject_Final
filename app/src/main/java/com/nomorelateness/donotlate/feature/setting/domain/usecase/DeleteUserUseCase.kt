package com.nomorelateness.donotlate.feature.setting.domain.usecase

import com.nomorelateness.donotlate.core.domain.repository.UserRepository
import com.nomorelateness.donotlate.feature.main.presentation.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<Result<Unit>> = flow {
        try {
            userRepository.deleteUserData()
            userRepository.deleteUserAccount()
            emit(Result.Success(Unit))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }
}