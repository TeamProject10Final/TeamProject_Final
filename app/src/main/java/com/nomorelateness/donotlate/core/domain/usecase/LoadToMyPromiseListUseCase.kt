package com.nomorelateness.donotlate.core.domain.usecase

import com.nomorelateness.donotlate.core.domain.model.PromiseRoomEntity
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class LoadToMyPromiseListUseCase
@Inject
constructor(private val firebaseDataRepository: FirebaseDataRepository) {
    suspend operator fun invoke(uid: String): Flow<List<PromiseRoomEntity>> {
        return firebaseDataRepository.getMyPromiseListFromFireBase(uid)
    }
}