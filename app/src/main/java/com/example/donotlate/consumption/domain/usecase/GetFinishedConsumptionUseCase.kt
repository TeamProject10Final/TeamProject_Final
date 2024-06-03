package com.example.donotlate.consumption.domain.usecase

import com.example.donotlate.consumption.domain.repository.ConsumptionRepository

class GetFinishedConsumptionUseCase(private val repository: ConsumptionRepository) {
    operator fun invoke() = repository.getFinishedConsumption()
}