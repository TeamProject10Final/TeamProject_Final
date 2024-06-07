package com.example.donotlate.feature.room.presentation.model

import com.google.firebase.Timestamp

data class UserModel(
    val name: String,
    val email: String,
    val uId: String,
    val friend: List<String>,
    val count:Int,
    val continuousCounter:Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
)

