package com.example.donotlate.consumption.domain.repository

import androidx.lifecycle.LiveData
import com.example.donotlate.consumption.domain.entity.ConsumptionEntity
import kotlinx.coroutines.flow.Flow

interface ConsumptionRepository {

    fun getConsumptionData(): Flow<List<ConsumptionEntity>>

    suspend fun insertConsumptionData(consumption: ConsumptionEntity)

    suspend fun deleteConsumptionData(consumption: ConsumptionEntity)

    suspend fun getConsumptionById(historyId: String): ConsumptionEntity?

    suspend fun getConsumptionByCategory(category: String): List<ConsumptionEntity?>

    suspend fun getDataCount(): Int

    fun getLiveDataCount(): Flow<Int>

    fun getFinishedConsumption(): Flow<List<ConsumptionEntity>>
    fun getUnfinishedConsumption(): Flow<List<ConsumptionEntity>>

    fun getRecentFinishedConsumption(): Flow<List<ConsumptionEntity>>
    fun getRecentUnfinishedConsumption(): Flow<List<ConsumptionEntity>>

    fun getTotalPrice(): Flow<Long>

}