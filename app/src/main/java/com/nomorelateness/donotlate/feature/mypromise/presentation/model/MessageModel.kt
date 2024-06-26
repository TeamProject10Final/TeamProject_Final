package com.nomorelateness.donotlate.feature.mypromise.presentation.model

import com.google.firebase.Timestamp


data class MessageModel(
    val messageId: String,
    val senderId: String,
    val senderName: String,
    val sendTimestamp: Timestamp,
    val contents: String,
    val senderProfileUrl: String
)


