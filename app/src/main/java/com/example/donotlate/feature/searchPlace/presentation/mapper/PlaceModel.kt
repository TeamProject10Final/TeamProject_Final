package com.example.donotlate.feature.searchPlace.presentation.mapper

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "SearchList")
@Parcelize
data class PlaceModel(

    val lat: Double,
    val lng: Double,
    val name: String?,
    val address: String,
    val rating: Double?,
    val phoneNumber: String?,
    val img: String?,
    val description: List<String>?
) : Parcelable
