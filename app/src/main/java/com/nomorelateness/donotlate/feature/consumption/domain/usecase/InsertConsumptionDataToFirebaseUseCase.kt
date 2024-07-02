package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.entity.FirebaseConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository2
import kotlinx.coroutines.flow.Flow

class InsertConsumptionDataToFirebaseUseCase(private val repository2: ConsumptionRepository2) {
    suspend operator fun invoke(insertEntity: FirebaseConsumptionEntity): Flow<Result<Boolean>> {
        return repository2.insertConsumption(insertEntity)
    }
}
