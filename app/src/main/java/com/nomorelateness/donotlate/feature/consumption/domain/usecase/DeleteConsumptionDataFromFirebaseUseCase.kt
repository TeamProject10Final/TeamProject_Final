package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.entity.FirebaseConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository2
import kotlinx.coroutines.flow.Flow

class DeleteConsumptionDataFromFirebaseUseCase(private val repository2: ConsumptionRepository2) {
    suspend operator fun invoke(deleteEntity: FirebaseConsumptionEntity): Flow<Result<Boolean>> {
        return repository2.deleteConsumption(deleteEntity)
    }
}