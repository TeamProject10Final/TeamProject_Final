package com.example.donotlate.core.domain.model

import com.google.firebase.Timestamp

data class UserEntity(
    val name: String,
    val email: String,
    val uid: String,
    val friends: List<String>,
    val count:Int,
    val continuousCounter:Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
)


