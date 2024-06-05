package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GooglePlace(
    @SerializedName("html_attributions")
    //사용자에게 표시되어야 하는 이 목록
    val htmlAttributions: List<String>,

    @SerializedName("next_page_token")
    //최대20개 사용할수있는 토큰
    val nextPageToken: String,
    //장소검색
    val results: List<ResultReponse>,
    //요청이 실패한 이유를 추적
    val status: String
): Parcelable