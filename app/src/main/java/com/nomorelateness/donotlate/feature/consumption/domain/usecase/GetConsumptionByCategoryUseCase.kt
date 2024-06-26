package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetConsumptionByCategoryUseCase
constructor(private val repository: ConsumptionRepository) {

    suspend operator fun invoke(category: String) = repository.getConsumptionByCategory(category)
}