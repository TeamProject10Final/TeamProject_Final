package com.nomorelateness.donotlate.core.domain.model

import com.google.firebase.Timestamp

data class FriendRequestEntity(
    val requestId:String,
    val toId: String,
    val fromId: String,
    val status: String,
    val requestTime: Timestamp,
    val fromUserName: String
)
