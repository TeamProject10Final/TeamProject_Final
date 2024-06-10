package com.example.donotlate.feature.auth.data.model

data class RegisterUserDTO(
    val name: String,
    val email: String,
    val uId: String,
    val count:Int = 0,
    val friend: List<String> = listOf(),
    val currentCount:Int = 0,
    val createdAt: com.google.firebase.Timestamp
)
