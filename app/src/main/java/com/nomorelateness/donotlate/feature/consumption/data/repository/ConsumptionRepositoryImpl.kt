package com.nomorelateness.donotlate.feature.consumption.data.repository

import android.content.Context
import com.nomorelateness.donotlate.feature.consumption.data.database.ConsumptionRoomDatabase
import com.nomorelateness.donotlate.feature.consumption.data.database.RoomDao
import com.nomorelateness.donotlate.feature.consumption.data.database.RoomEntity
import com.nomorelateness.donotlate.feature.consumption.data.database.asConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.entity.ConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ConsumptionRepositoryImpl(val context: Context) : ConsumptionRepository {
    private val roomDB: ConsumptionRoomDatabase = ConsumptionRoomDatabase.getInstance(context)!!
    private val roomDao: RoomDao = roomDB.getRoomDao()

    override fun getConsumptionData(): Flow<List<ConsumptionEntity>> {
        return roomDao.getAllData().map { it ->
            it.map(RoomEntity::asConsumptionEntity)
        }
    }

    override suspend fun insertConsumptionData(consumption: ConsumptionEntity) {
        val roomData = RoomEntity(
            consumption.historyId,
            consumption.detail,
            consumption.date,
            consumption.category,
            consumption.total,
            consumption.isPenalty,
            consumption.penalty,
            consumption.number,
            consumption.price,
            consumption.isFinished
        )
        roomDao.insertData(roomData)
    }

    override suspend fun deleteConsumptionData(consumption: ConsumptionEntity) {

        val roomData = RoomEntity(
            consumption.historyId,
            consumption.detail,
            consumption.date,
            consumption.category,
            consumption.total,
            consumption.isPenalty,
            consumption.penalty,
            consumption.number,
            consumption.price,
            consumption.isFinished
        )
        roomDao.deleteData(roomData)
    }


    override suspend fun getConsumptionById(historyId: String): ConsumptionEntity? {
        return roomDao.getDataById(historyId)?.asConsumptionEntity()
    }

    override suspend fun getConsumptionByCategory(category: String): List<ConsumptionEntity> {
        return roomDao.getDataByCategory(category).map(RoomEntity::asConsumptionEntity)
    }

    override suspend fun getDataCount(): Int {
        return roomDao.getDataCount().first()
    }


    override fun getRecentFinishedConsumption(): Flow<List<ConsumptionEntity>> {
        return roomDao.getRecentFinishedConsumptions().map {
            it.map(RoomEntity::asConsumptionEntity)
        }
    }


    override fun getRecentUnfinishedConsumption(): Flow<List<ConsumptionEntity>> {
        return roomDao.getRecentUnfinishedConsumptions().map {
            it.map(RoomEntity::asConsumptionEntity)
        }
    }


    // 데이터베이스에서 데이터의 총 가격을 반환하는 메서드
    override fun getTotalPrice(): Flow<Long> {
        return roomDao.getTotalPriceFromData()
    }

    // 데이터베이스에서 데이터의 개수를 반환하는 메서드
    override fun getLiveDataCount(): Flow<Int> {
        return roomDao.getDataCount()
    }

    override suspend fun toggleIsFinished(consumption: ConsumptionEntity) {
        val roomData = RoomEntity(
            historyId = consumption.historyId,
            detail = consumption.detail,
            date = consumption.date,
            category = consumption.category,
            total = consumption.total,
            isPenalty = consumption.isPenalty,
            penalty = consumption.penalty,
            number = consumption.number,
            price = consumption.price,
            isFinished = !consumption.isFinished
        )
        roomDao.updateData(roomData)
    }

    override suspend fun ClearAllConsumptions() {
        roomDao.clearAll()
    }
}