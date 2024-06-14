package com.example.donotlate.core.domain.model

import com.google.firebase.Timestamp

data class PromiseRoomEntity (
    val roomTitle: String,
    val roomCreatedAt: Timestamp,
    val promiseTime: String,
    val promiseDate: String,
    val destination: String, //<-- 목적지 주소 <지번 등의 텍스트 주소>
    val destinationLat: Double, // <-- 위도
    val destinationLng: Double, // <-- 경도
    val penalty: String,
    val participants:List<String>
)