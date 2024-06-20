package com.example.donotlate.feature.mypromise.presentation.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseModel(
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
//    val isArrived: Map<String, Boolean>,
//    val sss: HashMap<String, Boolean>

    /*if(약속시간 != 현재시간) {

    }

    올 트루?
    한명씩 도착 버튼 누를 때마다  +1
    참여자 리스트의 사이즈 수 == 도착이 같아질 때

    위에다가 위젯하나 띄워서 거기에 지각자가 있으면 표기 없으면
    모두다 도착 잘 했습니다!

    인원수만큼 돌려서 매핑,

    일단은 다 와와야 메시징 기능을 비활성화 ->*/


) : Parcelable {
    constructor() : this("", "", Timestamp.now(), "", "", "", 0.0, 0.0, "", listOf())
}






