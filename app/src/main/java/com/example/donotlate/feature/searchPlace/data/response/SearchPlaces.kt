package com.example.donotlate.feature.searchPlace.data.response

import com.google.gson.annotations.SerializedName

data class SearchPlaces(
    @SerializedName("places")
    val places : List<Places>?
)

data class Places(

    @SerializedName("types")
    val types : List<String>?,

    @SerializedName("nationalPhoneNumber")
    val nationalPhoneNumber : String?,

    @SerializedName("formattedAddress")
    val formattedAddress: String,

    @SerializedName("location")
    val location : SearchLocation?,

    @SerializedName("rating")
    val rating : Double?,

    @SerializedName("regularOpeningHours")
    val regularOpeningHours : RegularOpeningHours?,

    @SerializedName("displayName")
    val displayName : DisplayName?,

    @SerializedName("photos")
    val photos : List<SearchPhotos>?,

    )

data class SearchLocation(
    @SerializedName("latitude")
    val latitude : Double?,
    @SerializedName("longitude")
    val longitude : Double?
)

data class RegularOpeningHours(
    @SerializedName("openNow")
    val openNow: Boolean?,
    @SerializedName("periods")
    val periods: List<Periods>?,
    @SerializedName("weekdayDescriptions")
    val weekdayDescriptions : List<String>?
)

data class Periods(
    @SerializedName("open")
    val open: Open?,
    @SerializedName("close")
    val close: Close?,
)

data class Open(
    @SerializedName("day")
    val day: Int?,
    @SerializedName("hour")
    val hour: Int?,
    @SerializedName("minute")
    val minute: Int?,
)

data class Close(
    @SerializedName("day")
    val day: Int?,
    @SerializedName("hour")
    val hour: Int?,
    @SerializedName("minute")
    val minute: Int?,
)

data class DisplayName(
    @SerializedName("text")
    val text: String?,
    @SerializedName("languageCode")
    val languageCode: String?,
)

data class SearchPhotos(
    @SerializedName("name")
    val name : String?,
    @SerializedName("widthPx")
    val widthPx : Long?,
    @SerializedName("heightPx")
    val heightPx : Long?,
    @SerializedName("authorAttributions")
    val authorAttributions : List<AuthorAttributions>?,
)

data class AuthorAttributions(
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("uri")
    val uri: String?,
    @SerializedName("photoUri")
    val photoUri: String?,
)
