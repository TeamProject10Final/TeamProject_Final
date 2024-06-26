package com.nomorelateness.donotlate.feature.mypromise.presentation.model

import com.google.firebase.Timestamp

data class UserModel(
    val name: String,
    val email: String,
    val uid: String,
    val friends: List<String>,
    val count: Int,
    val continuousCounter: Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
) {
    constructor() : this("", "", "", listOf(), 0, 0, Timestamp.now())
}
