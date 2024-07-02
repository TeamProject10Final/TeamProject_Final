package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class AcceptFriendRequestsUseCase
@Inject
constructor(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(requestId: String): Flow<Boolean> {
        return firebaseDataRepository.acceptFriendRequest(requestId)
    }
}
