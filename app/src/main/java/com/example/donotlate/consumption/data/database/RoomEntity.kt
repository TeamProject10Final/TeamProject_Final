package com.example.donotlate.consumption.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_history")
data class RoomEntity(

    @PrimaryKey val historyId: Int,
    @ColumnInfo(name = "detail") val detail: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "total") val total: String,
    @ColumnInfo(name = "isPenalty") val isPenalty : Boolean = false,
    @ColumnInfo(name = "penalty") val penalty: String?,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "price") val price: Int = 0,
    @ColumnInfo(name = "isFinished") val isFinished: Boolean = false
)
