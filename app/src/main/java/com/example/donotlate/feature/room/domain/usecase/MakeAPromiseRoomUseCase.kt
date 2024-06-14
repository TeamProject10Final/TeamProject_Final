package com.example.donotlate.feature.room.domain.usecase

import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import com.example.donotlate.feature.main.presentation.model.UserModel
import kotlinx.coroutines.flow.Flow

class MakeAPromiseRoomUseCase (private val firebaseDataRepository: FirebaseDataRepository){
    suspend operator fun invoke(roomTitle: String, promiseTime: String, promiseDate: String, destination: String, destinationLat: Double, destinationLng: Double, penalty: String, participants: List<String> ): Flow<Boolean>{
        return firebaseDataRepository.makeAPromiseRoom(roomTitle, promiseTime, promiseDate, destination, destinationLat, destinationLng, penalty, participants)
    }
}
