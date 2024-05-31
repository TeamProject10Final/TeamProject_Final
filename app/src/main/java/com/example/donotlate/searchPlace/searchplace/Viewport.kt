package com.example.donotlate.searchPlace.searchplace

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
): Parcelable