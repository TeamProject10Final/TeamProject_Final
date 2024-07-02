package com.nomorelateness.donotlate.feature.consumption.domain.usecase

import com.nomorelateness.donotlate.feature.consumption.domain.entity.ConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository2
import kotlinx.coroutines.flow.first

class ConsumptionDataSyncUseCase(
    private val repository: ConsumptionRepository,
    private val repository2: ConsumptionRepository2
) {
    suspend operator fun invoke() {
        val firebaseConsumptions = repository2.getAllConsumption().first()
        firebaseConsumptions.forEach { firebaseConsumption ->
            val consumptionEntity = ConsumptionEntity(
                historyId = firebaseConsumption.historyId,
                detail = firebaseConsumption.detail,
                date = firebaseConsumption.date,
                category = firebaseConsumption.category,
                total = firebaseConsumption.total,
                isPenalty = firebaseConsumption.penaltyFlag,
                penalty = firebaseConsumption.penalty,
                number = firebaseConsumption.number,
                price = firebaseConsumption.price,
                isFinished = firebaseConsumption.hasFinished
            )
            repository.insertConsumptionData(consumptionEntity)
        }
    }
}