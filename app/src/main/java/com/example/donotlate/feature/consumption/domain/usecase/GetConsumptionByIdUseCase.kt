package com.example.donotlate.feature.consumption.domain.usecase

import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetConsumptionByIdUseCase
constructor(private val repository: ConsumptionRepository) {
    suspend operator fun invoke(historyId: String) = repository.getConsumptionById(historyId)
}