package com.example.donotlate.searchPlace.searchplace

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    //사진높이
    val height: Int,

    @SerializedName("htmlAttributions")
    //사진 HTML
    val html_attributions: List<String>,

    @SerializedName("photoReference")
    //사진요청
    val photo_reference: String,
    //사진너비
    val width: Int
) : Parcelable