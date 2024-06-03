package com.example.donotlate.consumption.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.donotlate.consumption.domain.entity.ConsumptionEntity

@Dao
interface RoomDao {
    @Query("SELECT * FROM calculation_history")
    fun getAllData(): LiveData<List<RoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(roomData: RoomEntity)

    @Delete
    suspend fun deleteData(roomData: RoomEntity)

    @Query("SELECT * FROM calculation_history WHERE historyId = :historyId LIMIT 1")
    suspend fun getDataById(historyId: String): RoomEntity?

    @Query("SELECT * FROM calculation_history WHERE category = :category")
    suspend fun getDataByCategory(category: String): List<RoomEntity?>

    @Query("SELECT COUNT(*) FROM calculation_history")
    suspend fun getDataCount(): Int

    @Query("SELECT * FROM calculation_history WHERE isFinished = 1")
    fun getFinishedData(): LiveData<List<RoomEntity>>

    @Query("SELECT * FROM calculation_history WHERE isFinished = 0")
    fun getUnfinishedData(): LiveData<List<RoomEntity>>

    @Query("SELECT * FROM calculation_history WHERE isFinished = 1 ORDER BY date DESC LIMIT 5")
    fun getRecentFinishedConsumptions(): LiveData<List<ConsumptionEntity>>
    //Flow

    @Query("SELECT * FROM calculation_history WHERE isFinished = 0 ORDER BY date")
    fun getRecentUnfinishedConsumptions(): LiveData<List<ConsumptionEntity>>


    // 데이터베이스에서 데이터의 총 가격을 가져오는 쿼리 메서드
    @Query("SELECT SUM(price) FROM calculation_history")
    fun getTotalPriceFromData(): LiveData<Long>

    // 데이터베이스에서 데이터의 개수를 가져오는 쿼리 메서드...  인데 LIVEDATA
    @Query("SELECT COUNT(*) FROM calculation_history")
    fun getDataCountFromData(): LiveData<Int>
}