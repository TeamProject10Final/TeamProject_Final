package com.example.donotlate.feature.chatroom.presentation.model

import android.os.Parcelable
import com.example.donotlate.feature.main.presentation.model.UserModel
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


data class ChatRoomModel(
    val roomTitle: String,
    val roomCreatedAt: Timestamp,
    val promiseTime: String,
    val promiseDate: String,
    val destination: String, //<-- 목적지 주소 <지번 등의 텍스트 주소>
    val destinationLat: Double, // <-- 위도
    val destinationLng: Double, // <-- 경도
    val penalty: String,
    val participants:List<UserModel>
){
    constructor(): this("", Timestamp.now(), "", "", "", 0.0, 0.0, "", listOf())
}

private var roomTitle = "" //"약속잡기"





