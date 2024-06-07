package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    //사진높이
    val height: Int?,

    @SerializedName("html_attributions")
    //사진 HTML
    val htmlAttributions: List<String>?,

    @SerializedName("photo_reference")
    //사진요청
    val photoReference: String?,
    //사진너비
    val width: Int?
) : Parcelable