package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.example.donotlate.feature.consumption.domain.entity.ConsumptionEntity
import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class ToggleIsFinishedUseCase(private val repository: ConsumptionRepository) {
    suspend operator fun invoke(consumption: ConsumptionEntity) {
        repository.toggleIsFinished(consumption)
    }
}