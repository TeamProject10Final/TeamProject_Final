package com.nomorelateness.donotlate.core.domain.usecase

import android.util.Log
import com.nomorelateness.donotlate.core.domain.model.UserEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GetFriendsListFromFirebaseUseCase(private val firebaseDataRepository: FirebaseDataRepository) {

    suspend operator fun invoke(uid: String): Flow<List<UserEntity>> {
        return firebaseDataRepository.getFriendsListFromFirebase(uid).onEach { friends->
            Log.d("GetFriendsListFromFirebaseUseCase", "Friends List Size: ${friends.size}")}

    }

}