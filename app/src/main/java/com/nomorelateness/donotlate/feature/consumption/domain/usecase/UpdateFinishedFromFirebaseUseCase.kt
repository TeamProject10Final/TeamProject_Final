package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.entity.FirebaseConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository2

class UpdateFinishedFromFirebaseUseCase(private val repository2: ConsumptionRepository2) {
    suspend operator fun invoke(consumptionEntity: FirebaseConsumptionEntity) {
        return repository2.updateFinished(consumptionEntity)
    }
}