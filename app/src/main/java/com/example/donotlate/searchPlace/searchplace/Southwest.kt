package com.example.donotlate.searchPlace.searchplace

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Southwest(
    val lat: Double,
    val lng: Double
): Parcelable