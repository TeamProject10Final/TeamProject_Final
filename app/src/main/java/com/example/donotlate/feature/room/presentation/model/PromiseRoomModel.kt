package com.example.donotlate.feature.room.presentation.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseRoomModel(
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
    // key = uid, value = name
    // uid를 키로 검색해서, 해당 인덱스 삭제?
//    val participantsNames: Map<String, String>
):Parcelable
