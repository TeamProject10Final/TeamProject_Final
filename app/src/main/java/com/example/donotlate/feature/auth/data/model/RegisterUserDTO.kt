package com.example.donotlate.feature.auth.data.model

import com.google.firebase.Timestamp

data class RegisterUserDTO(
    val name: String,
    val email: String,
    val uid: String,
    val friends: List<String> = listOf(),
    val count:Int = 0,
    val continuousCounter: Int = 0,
    val createdAt: Timestamp,
    val profileImgUrl: String = "",
)
