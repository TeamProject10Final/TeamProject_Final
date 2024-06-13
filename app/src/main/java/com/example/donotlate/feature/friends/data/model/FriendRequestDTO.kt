package com.example.donotlate.feature.friends.data.model

import com.google.firebase.Timestamp

data class FriendRequestDTO (
    val toId: String,
    val fromId: String,
    val status: String,
    val requestTime: Timestamp,
    val fromUserName: String
)