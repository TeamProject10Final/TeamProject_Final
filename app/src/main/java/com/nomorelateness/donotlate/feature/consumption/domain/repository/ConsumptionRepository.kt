package com.nomorelateness.donotlate.feature.consumption.domain.repository

import com.nomorelateness.donotlate.feature.consumption.domain.entity.ConsumptionEntity
import kotlinx.coroutines.flow.Flow

interface ConsumptionRepository {

    fun getConsumptionData(): Flow<List<ConsumptionEntity>>

    suspend fun insertConsumptionData(consumption: ConsumptionEntity)

    suspend fun deleteConsumptionData(consumption: ConsumptionEntity)

    suspend fun getConsumptionById(historyId: String): ConsumptionEntity?

    suspend fun getConsumptionByCategory(category: String): List<ConsumptionEntity?>

    suspend fun getDataCount(): Int

    fun getLiveDataCount(): Flow<Int>

    fun getRecentFinishedConsumption(): Flow<List<ConsumptionEntity>>
    fun getRecentUnfinishedConsumption(): Flow<List<ConsumptionEntity>>

    fun getTotalPrice(): Flow<Long>

    suspend fun toggleIsFinished(consumption: ConsumptionEntity)
}