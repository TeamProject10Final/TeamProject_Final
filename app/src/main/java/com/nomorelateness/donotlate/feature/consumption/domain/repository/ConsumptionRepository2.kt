package com.nomorelateness.donotlate.feature.consumption.domain.repository

import com.nomorelateness.donotlate.feature.consumption.domain.entity.FirebaseConsumptionEntity
import kotlinx.coroutines.flow.Flow

interface ConsumptionRepository2 {
    suspend fun insertConsumption(consumption: FirebaseConsumptionEntity): Flow<Result<Boolean>>
    suspend fun deleteConsumption(consumption: FirebaseConsumptionEntity): Flow<Result<Boolean>>
    suspend fun updateFinished(consumption: FirebaseConsumptionEntity)
    suspend fun getAllConsumption(): Flow<List<FirebaseConsumptionEntity>>
}