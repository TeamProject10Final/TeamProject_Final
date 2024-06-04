package com.example.donotlate.core.domain.model

import java.sql.Timestamp

data class User(
    val name: String,
    val email: String,
    val uId: String,
    val friend: List<String>,
    val count:Int,
    val currentCount:Int,
    val createdAt: Timestamp
)

data class Location(
    val locationLatitude: Long,
    val locationLongitude: Long
)

