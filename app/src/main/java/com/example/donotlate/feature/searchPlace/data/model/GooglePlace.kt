package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GooglePlace(
    @SerializedName("htmlAttributions")
    //사용자에게 표시되어야 하는 이 목록
    val html_attributions: List<String>,

    @SerializedName("nextPageToken")
    //최대20개 사용할수있는 토큰
    val next_page_token: String,
    //장소검색
    val results: List<Result>,
    //요청이 실패한 이유를 추적
    val status: String
): Parcelable