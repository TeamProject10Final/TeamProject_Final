package com.example.donotlate.consumption.domain.usecase

import com.example.donotlate.consumption.domain.entity.ConsumptionEntity
import com.example.donotlate.consumption.domain.repository.ConsumptionRepository

class InsertConsumptionUseCase(private val repository: ConsumptionRepository) {
    suspend operator fun invoke(consumptionEntity: ConsumptionEntity) = repository.insertConsumptionData(consumptionEntity)
}