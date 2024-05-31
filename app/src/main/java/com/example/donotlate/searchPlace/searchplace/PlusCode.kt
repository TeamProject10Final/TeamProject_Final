package com.example.donotlate.searchPlace.searchplace

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlusCode(
    @SerializedName("compoundCode")
    val compound_code: String,
    @SerializedName("globalCode")
    val global_code: String
): Parcelable