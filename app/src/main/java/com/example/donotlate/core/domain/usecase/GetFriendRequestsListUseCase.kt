package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.data.response.FriendRequestWithUserDataEntity
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class GetFriendRequestsListUseCase(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(toId: String): Flow<List<FriendRequestWithUserDataEntity>> = flow {
        firebaseDataRepository.getFriendRequestsList(toId).collect { requests ->
            val requestWithUserData = requests.mapNotNull { request ->
                val userData =
                    firebaseDataRepository.getUserDataById(request.fromId).toList().firstOrNull()
                userData?.let { FriendRequestWithUserDataEntity(request, it) }
            }
            emit(requestWithUserData)
        }
    }
}