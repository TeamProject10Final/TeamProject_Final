package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpeningHours(
    @SerializedName("open_now")
    //현재 시간에 해당 장소가 열려 있는지 여부
    val openNow: Boolean?
): Parcelable