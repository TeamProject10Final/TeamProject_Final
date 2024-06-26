package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetLiveDataCountUseCase
constructor(private val repository: ConsumptionRepository) {
    operator fun invoke() = repository.getLiveDataCount()
}

