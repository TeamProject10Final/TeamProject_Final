package com.example.donotlate.feature.friends.presentation.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendsUserModel(
    val name: String,
    val email: String,
    val uid: String,
    val friends: List<String>,
    val count:Int,
    val continuousCounter:Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
): Parcelable {
    constructor(): this("","","", listOf(),0,0, Timestamp.now(),"")
}