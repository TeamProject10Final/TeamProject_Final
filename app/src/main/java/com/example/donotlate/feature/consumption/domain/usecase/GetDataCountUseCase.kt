package com.example.donotlate.feature.consumption.domain.usecase

import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetDataCountUseCase
constructor(private val repository: ConsumptionRepository) {
        suspend operator fun invoke() = repository.getDataCount()
}