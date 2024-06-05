package com.example.donotlate.feature.searchPlace.presentation

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "SearchList")
@Parcelize
data class SearchModel(

    val lat: String?,
    val lng: String?,
    val name: String?,
    val address: String?,
    val rating: String?,
    val phoneNumber: String?,
    val img: List<List<String>>?
) : Parcelable
