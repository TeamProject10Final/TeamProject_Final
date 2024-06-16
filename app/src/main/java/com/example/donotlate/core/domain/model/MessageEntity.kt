package com.example.donotlate.core.domain.model

import com.google.firebase.Timestamp

data class MessageEntity(
    val messageId: String,
    val senderId: String,
    val senderName: String,
    val sendTimestamp: Timestamp,
    val contents: String,
    val senderProfileUrl: String
)
