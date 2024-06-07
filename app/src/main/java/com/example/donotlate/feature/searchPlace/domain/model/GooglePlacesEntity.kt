package com.example.donotlate.feature.searchPlace.domain.model

data class GooglePlacesEntity(

    val htmlAttributions: List<String>?,
    val nextPageToken: String?,
    val results: List<ResultsEntity>?,
    val status: String?
)

data class ResultsEntity(

    val formattedAddress: String?,
    val geometry: GeometryEntity?,
    val businessStatus: String?,
    val icon: String?,
    val iconBackgroundColor: String?,
    val iconMaskBaseUri: String?,
    val name: String?,
    val openingHours: OpeningHoursEntity?,
    val photos: List<PhotoEntity>?,
    val placeId: String?,
    val priceLevel: Int?,
    val rating: Double?,
    val types: List<String>?,
    val userRatingsTotal: Int?,
    val phoneNumber: String?

    )

data class PhotoEntity(

    val height: Int?,
    val htmlAttributions: List<String>?,
    val photoReference: String?,
    val width: Int?

)

data class GeometryEntity(
    val location: LocationEntity?
)

data class LocationEntity(
    val lat: Double?,
    val lng: Double?
)

data class OpeningHoursEntity(
    val openNow: Boolean?
)
