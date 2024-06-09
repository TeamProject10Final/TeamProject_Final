package com.example.donotlate.feature.searchPlace.presentation.data

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "SearchList")
@Parcelize
data class PlaceModel(

    val lat: String?,
    val lng: String?,
    val name: String?,
    val address: String?,
    val rating: Double?,
    val phoneNumber: String?,
    val img: String?
) : Parcelable


@Entity(tableName = "SearchChipList")
@Parcelize
data class PlacesChipModel(

    val types : List<String>?,
    val lat: String?,
    val lng: String?,
    val name: String?,
    val address: String?,
    val rating: Double?,
    val phoneNumber: String?,
    val img: String?
): Parcelable