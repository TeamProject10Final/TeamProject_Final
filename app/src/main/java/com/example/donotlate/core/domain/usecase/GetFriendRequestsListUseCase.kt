package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.data.response.FriendRequestWithUserDataEntity
import com.example.donotlate.core.domain.repository.FirebaseDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class GetFriendRequestsListUseCase(private val firebaseDataRepository: FirebaseDataRepository) {

    // repositoryImpl 옮기기(수정하기) <-- for 문 압수
    suspend operator fun invoke(toId: String): Flow<List<FriendRequestWithUserDataEntity>> = flow {
        firebaseDataRepository.getFriendRequestsList(toId).collect() { requests ->
            val requestWithUserData = requests.mapNotNull { request ->
                val userData = firebaseDataRepository.getUserDataById(request.fromId).toList().firstOrNull()
                userData?.let { FriendRequestWithUserDataEntity(request, it) }
            }
            emit(requestWithUserData)
        }
    }
}