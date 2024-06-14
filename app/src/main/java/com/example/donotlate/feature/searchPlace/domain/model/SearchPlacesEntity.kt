package com.example.donotlate.feature.searchPlace.domain.model

data class SearchPlacesEntity(
    val places : List<PlacesEntity>?
)

data class PlacesEntity(

    val types : List<String>?,
    val formattedAddress : String,
    val nationalPhoneNumber: String?,
    val location : SearchLocationEntity?,
    val rating : Double?,
    val regularOpeningHours : RegularOpeningHoursEntity?,
    val displayName : DisplayNameEntity?,
    val photos : List<SearchPhotosEntity>?,

    )

data class SearchLocationEntity(

    val latitude : Double?,
    val longitude : Double?
)

data class RegularOpeningHoursEntity(

    val openNow: Boolean?,
    val periods: List<PeriodsEntity>?,
    val weekdayDescriptions : List<String>?
)

data class PeriodsEntity(

    val open: OpenEntity?,
    val close: CloseEntity?,
)

data class OpenEntity(

    val day: Int?,
    val hour: Int?,
    val minute: Int?,
)

data class CloseEntity(

    val day: Int?,
    val hour: Int?,
    val minute: Int?,
)

data class DisplayNameEntity(

    val text: String?,
    val languageCode: String?,
)

data class SearchPhotosEntity(

    val name : String?,
    val widthPx : Long?,
    val heightPx : Long?,
    val authorAttributions : List<AuthorAttributionsEntity>?,
)

data class AuthorAttributionsEntity(

    val displayName: String?,
    val uri: String?,
    val photoUri: String?,
)
