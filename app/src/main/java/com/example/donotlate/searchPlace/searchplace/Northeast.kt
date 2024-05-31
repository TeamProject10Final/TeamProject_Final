package com.example.donotlate.searchPlace.searchplace

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Northeast(
    val lat: Double,
    val lng: Double
): Parcelable