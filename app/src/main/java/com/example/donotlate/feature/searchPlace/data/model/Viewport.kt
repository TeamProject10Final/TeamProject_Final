package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
): Parcelable