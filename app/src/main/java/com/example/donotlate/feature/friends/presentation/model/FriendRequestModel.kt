package com.example.donotlate.feature.friends.presentation.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendRequestModel(
    val requestId:String,
    val toId: String,
    val fromId: String,
    val status: String,
    val requestTime: Timestamp,
    val fromUserName: String
) : Parcelable {
    constructor() : this("","", "", "", Timestamp.now(), "")
}
