package com.example.donotlate.core.domain.model

import com.google.firebase.Timestamp


data class UserEntity(
    val name: String,
    val email: String,
    val uId: String,
    val friend: List<String>,
    val count:Int,
    val continuousCounter:Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
)

data class Location(
    val locationLatitude: Long,
    val locationLongitude: Long
)

