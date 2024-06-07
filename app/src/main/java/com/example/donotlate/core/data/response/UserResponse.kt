package com.example.donotlate.core.data.response

import com.google.firebase.Timestamp


data class UserResponse(
    val name: String,
    val email: String,
    val uId: String,
    val friend: List<String>,
    val count:Int,
    val continuousCounter:Int,
    val createdAt: Timestamp,
    val profileImgUrl: String = ""
){
    constructor(): this("","","", listOf(),0,0, Timestamp.now(),"")
}

data class Location(
    val locationLatitude: Long,
    val locationLongitude: Long
)



