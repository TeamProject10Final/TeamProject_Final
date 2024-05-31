package com.example.donotlate.searchPlace.searchplace

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Geometry(
    val location: Location,
    val viewport: Viewport
) :Parcelable