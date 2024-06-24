package com.example.donotlate.core.data.response

import com.google.firebase.Timestamp

data class PromiseRoomResponse(
    val roomId: String,
    val roomTitle: String,
    val roomCreatedAt: Timestamp,
    val promiseTime: String,
    val promiseDate: String,
    val destination: String, //<-- 목적지 주소 <지번 등의 텍스트 주소>
    val destinationLat: Double, // <-- 위도
    val destinationLng: Double, // <-- 경도
    val penalty: String,
    val participants: List<String>,
    val hasArrived: Map<String, Boolean> = mutableMapOf(),
    val participantsNames: Map<String, String> = mutableMapOf()
) {
    constructor() : this(
        "",
        "",
        Timestamp.now(),
        "",
        "",
        "",
        0.0,
        0.0,
        "",
        listOf(),
        mutableMapOf(),
        mutableMapOf()
    )
}
