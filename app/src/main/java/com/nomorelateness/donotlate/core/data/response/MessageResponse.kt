package com.nomorelateness.donotlate.core.data.response

import com.google.firebase.Timestamp

data class MessageResponse(
    val messageId: String,
    val senderId: String,
    val senderName: String,
    val sendTimestamp: Timestamp,
    val contents: String,
    val senderProfileUrl: String
) {
    constructor() : this("", "", "", Timestamp.now(), "", "")
}
