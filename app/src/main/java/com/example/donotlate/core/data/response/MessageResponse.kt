package com.example.donotlate.core.data.response

import com.google.firebase.Timestamp

data class MessageResponse(
    val senderName: String,
    val sendTimestamp: Timestamp,
    val contents: String,
    val senderProfileUrl: String
) {
    constructor() : this("", Timestamp.now(), "", "")
}
