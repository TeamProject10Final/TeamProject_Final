package com.nomorelateness.donotlate.feature.auth.domain.useCase

import com.google.firebase.firestore.DocumentSnapshot
import com.nomorelateness.donotlate.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserWithKakaoUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(uid:String): Flow<DocumentSnapshot?>{
        return authRepository.getCurrentUserWithKakao(uid)
    }
}