package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetConsumptionByIdUseCase
constructor(private val repository: ConsumptionRepository) {
    suspend operator fun invoke(historyId: String) = repository.getConsumptionById(historyId)
}