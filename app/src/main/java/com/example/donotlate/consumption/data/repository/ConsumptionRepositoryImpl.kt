package com.example.donotlate.consumption.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.donotlate.consumption.data.database.ConsumptionRoomDatabase
import com.example.donotlate.consumption.data.database.RoomDao
import com.example.donotlate.consumption.data.database.RoomEntity
import com.example.donotlate.consumption.domain.entity.ConsumptionEntity
import com.example.donotlate.consumption.domain.repository.ConsumptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConsumptionRepositoryImpl(val context: Context) : ConsumptionRepository {
    private val roomDB: ConsumptionRoomDatabase = ConsumptionRoomDatabase.getInstance(context)!!
    private val roomDao: RoomDao = roomDB.getRoomDao()
    override fun getConsumptionData(): Flow<List<ConsumptionEntity>> {
        val entityList = roomDao.getAllData().map { room ->
            room.map {
                ConsumptionEntity(
                    it.historyId,
                    it.detail,
                    it.date,
                    it.category,
                    it.total,
                    it.isPenalty,
                    it.penalty,
                    it.number,
                    it.price,
                    it.isFinished
                )
            }
        }
        return entityList
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
        val roomEntity = roomDao.getDataById(historyId)
        return roomEntity?.let {
            ConsumptionEntity(
                it.historyId,
                it.detail,
                it.date,
                it.category,
                it.total,
                it.isPenalty,
                it.penalty,
                it.number,
                it.price,
                it.isFinished
            )
        }
    }

    override suspend fun getConsumptionByCategory(category: String): List<ConsumptionEntity?> {
        val categoryEntityList = roomDao.getDataByCategory(category)
        return categoryEntityList.map {
            it?.let {
                ConsumptionEntity(
                    it.historyId,
                    it.detail,
                    it.date,
                    it.category,
                    it.total,
                    it.isPenalty,
                    it.penalty,
                    it.number,
                    it.price,
                    it.isFinished
                )
            }
        }
    }

    override suspend fun getDataCount(): Int {
        return roomDao.getDataCount()
    }


    override fun getFinishedConsumption(): Flow<List<ConsumptionEntity>> {
        return roomDao.getFinishedData().map { roomEntities ->
            roomEntities.map { roomEntity ->
                ConsumptionEntity(
                    roomEntity.historyId,
                    roomEntity.detail,
                    roomEntity.date,
                    roomEntity.category,
                    roomEntity.total,
                    roomEntity.isPenalty,
                    roomEntity.penalty,
                    roomEntity.number,
                    roomEntity.price,
                    roomEntity.isFinished
                )
            }
        }
    }


    override fun getUnfinishedConsumption(): Flow<List<ConsumptionEntity>> {
        return roomDao.getUnfinishedData().map { roomEntities ->
            roomEntities.map { roomEntity ->
                ConsumptionEntity(
                    roomEntity.historyId,
                    roomEntity.detail,
                    roomEntity.date,
                    roomEntity.category,
                    roomEntity.total,
                    roomEntity.isPenalty,
                    roomEntity.penalty,
                    roomEntity.number,
                    roomEntity.price,
                    roomEntity.isFinished
                )
            }
        }
    }

    override fun getRecentFinishedConsumption(): Flow<List<ConsumptionEntity>> {
        return roomDao.getRecentFinishedConsumptions()
    }

    override fun getRecentUnfinishedConsumption(): Flow<List<ConsumptionEntity>> {
        return roomDao.getRecentUnfinishedConsumptions()
    }

    // 데이터베이스에서 데이터의 총 가격을 반환하는 메서드
    override fun getTotalPrice(): Flow<Long> {
        return roomDao.getTotalPriceFromData()
    }

    // 데이터베이스에서 데이터의 개수를 반환하는 메서드... 인데 LIVEDATA
    override fun getLiveDataCount(): Flow<Int> {
        return roomDao.getDataCountFromData()
    }


}