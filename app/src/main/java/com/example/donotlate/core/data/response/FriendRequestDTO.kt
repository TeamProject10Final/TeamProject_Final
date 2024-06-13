package com.example.donotlate.core.data.response

import com.google.firebase.Timestamp

data class FriendRequestDTO(
    val toId: String,
    val fromId: String,
    val status: String,
    val requestTime: Timestamp,
    val fromUserName: String
) {
    constructor(): this("","","",Timestamp.now(),"")
}
