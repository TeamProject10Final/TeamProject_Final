package com.example.donotlate.feature.searchPlace.presentation

data class GooglePlacesModel(

    val htmlAttributions: List<String>,
    val nextPageToken: String,
    val results: List<ResultModel>,
    val status: String
)

data class ResultModel(

    val businessStatus: String,
    val icon: String,
    val iconBackgroundColor: String,
    val iconMaskBaseUri: String,
    val name: String,
    val openingHours: OpeningHoursModel,
    val photos: List<PhotoModel>,
    val placeId: String,
    val priceLevel: Int,
    val rating: Double,
    val types: List<String>,
    val userRatingsTotal: Int,

    )

data class PhotoModel(

    val height: Int,
    val htmlAttributions: List<String>,
    val photoReference: String,
    val width: Int

)

data class GeometryModel(
    val location: LocationModel
)

data class LocationModel(
    val lat: Double,
    val lng: Double
)

data class OpeningHoursModel(
    val openNow: Boolean
)