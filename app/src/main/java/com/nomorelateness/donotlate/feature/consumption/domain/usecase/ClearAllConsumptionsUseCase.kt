package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class ClearAllConsumptionsUseCase(private val repository: ConsumptionRepository) {
    suspend operator fun invoke() {
        repository.ClearAllConsumptions()
    }
}