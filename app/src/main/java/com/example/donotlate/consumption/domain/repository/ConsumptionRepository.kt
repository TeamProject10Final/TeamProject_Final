package com.example.donotlate.consumption.domain.repository

import androidx.lifecycle.LiveData
import com.example.donotlate.consumption.domain.entity.ConsumptionEntity

interface ConsumptionRepository {

    fun getConsumptionData(): LiveData<List<ConsumptionEntity>>

    suspend fun insertConsumptionData(consumption: ConsumptionEntity)

    suspend fun deleteConsumptionData(consumption: ConsumptionEntity)

    suspend fun getConsumptionById(historyId: String): ConsumptionEntity?

    suspend fun getConsumptionByCategory(category: String): List<ConsumptionEntity?>

    suspend fun getDataCount(): Int

    fun getLiveDataCount(): LiveData<Int>

    fun getFinishedConsumption(): LiveData<List<ConsumptionEntity>>
    fun getUnfinishedConsumption(): LiveData<List<ConsumptionEntity>>

    fun getRecentFinishedConsumption(): LiveData<List<ConsumptionEntity>>
    fun getRecentUnfinishedConsumption(): LiveData<List<ConsumptionEntity>>

    fun getTotalPrice(): LiveData<Long>

}