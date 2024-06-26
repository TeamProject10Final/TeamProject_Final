package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetTotalPriceUseCase
constructor(private val repository: ConsumptionRepository){
    operator fun invoke() = repository.getTotalPrice()
}