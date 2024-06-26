package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.entity.ConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class DeleteConsumptionUseCase
constructor(private val repository: ConsumptionRepository) {
    suspend operator fun invoke(deleteEntity: ConsumptionEntity) =
        repository.deleteConsumptionData(deleteEntity)
}