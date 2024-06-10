package com.example.donotlate.feature.consumption.domain.usecase

import com.example.donotlate.feature.consumption.data.database.RoomDao
import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository

class GetConsumptionDataUseCase
constructor(private val repository: ConsumptionRepository) {
    operator fun invoke() = repository.getConsumptionData()
}