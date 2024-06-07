package com.example.donotlate.feature.main.presentation.model

data class ChatRoomModel (
    val roomTitle: String,
    val destination: String,
    val date:String,
    val time:String,
    val penalty: String,
    val participants: List<String>
)