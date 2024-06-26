package com.nomorelateness.donotlate.feature.main.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.UserEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase (private val firebaseDataRepository: FirebaseDataRepository){
    suspend operator fun invoke(userId:String): Flow<UserEntity?> {
        return firebaseDataRepository.getUserDataById(userId)
    }
}