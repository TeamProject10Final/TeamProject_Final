package com.example.donotlate.feature.consumption.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM calculation_history")
    fun getAllData(): Flow<List<RoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(roomData: RoomEntity)

    @Delete
    suspend fun deleteData(roomData: RoomEntity)

    @Query("SELECT * FROM calculation_history WHERE historyId = :historyId LIMIT 1")
    suspend fun getDataById(historyId: String): RoomEntity?

    @Query("SELECT * FROM calculation_history WHERE category = :category")
    suspend fun getDataByCategory(category: String): List<RoomEntity>

    @Query("SELECT COUNT(*) FROM calculation_history")
    suspend fun getDataCount(): Int

    @Query("SELECT * FROM calculation_history WHERE isFinished = 1 ORDER BY date DESC LIMIT 5")
    fun getRecentFinishedConsumptions(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM calculation_history WHERE isFinished = 0 ORDER BY date")
    fun getRecentUnfinishedConsumptions(): Flow<List<RoomEntity>>

    @Query("SELECT SUM(price) FROM calculation_history")
    suspend fun getTotalPriceFromData(): Long

    @Update
    suspend fun updateData(roomData: RoomEntity)
}