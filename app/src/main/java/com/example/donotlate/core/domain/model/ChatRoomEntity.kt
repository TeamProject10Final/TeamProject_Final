package com.example.donotlate.core.domain.model

data class ChatRoomEntity (
    val roomTitle: String,
    val destination: String,
    val date:String,
    val time:String,
    val penalty: String,
    val participants: List<String>
)