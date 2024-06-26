package com.nomorelateness.donotlate.feature.room.presentation.model

import com.google.firebase.Timestamp

data class RoomUserModel(
    val name: String,
    val email: String,
    val uId: String,
    val friend: List<String>,
    val count:Int,
    val continuousCounter:Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
) {
    constructor() : this("", "", "", emptyList(), 0, 0, Timestamp.now(), "")
}

