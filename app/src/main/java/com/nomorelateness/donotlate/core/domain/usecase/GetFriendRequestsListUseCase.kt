package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.FriendRequestWithUserDataEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

@ViewModelScoped
class GetFriendRequestsListUseCase
@Inject
constructor(private val firebaseDataRepository: FirebaseDataRepository) {

    // repositoryImpl 옮기기(수정하기) <-- for 문 압수
    suspend operator fun invoke(toId: String): Flow<List<FriendRequestWithUserDataEntity>> = flow {
        firebaseDataRepository.getFriendRequestsList(toId).collect() { requests ->
            val requestWithUserData = requests.mapNotNull { request ->
                val userData =
                    firebaseDataRepository.getUserDataById(request.fromId).toList().firstOrNull()
                userData?.let { FriendRequestWithUserDataEntity(request, it) }
            }
            emit(requestWithUserData)
        }
    }
}