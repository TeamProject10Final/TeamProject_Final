package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow

class LoadToMyPromiseListUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(): Flow<List<PromiseRoomEntity>>{
        return firebaseDataRepository.getMyPromiseListFromFireBase()
    }
}