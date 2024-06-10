package com.example.donotlate.feature.consumption.domain.usecase

import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetFinishedConsumptionUseCase
constructor(private val repository: ConsumptionRepository) {
    operator fun invoke() = repository.getRecentFinishedConsumption()
}