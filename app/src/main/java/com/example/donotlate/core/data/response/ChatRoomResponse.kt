package com.example.donotlate.core.data.response

data class ChatRoomResponse(
    val roomTitle: String,
    val destination: String,
    val date:String,
    val time:String,
    val penalty: String,
    val participants: List<String>
)
