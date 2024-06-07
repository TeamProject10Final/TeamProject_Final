package com.example.donotlate.feature.searchPlace.domain.model

import com.example.donotlate.feature.searchPlace.presentation.GeometryModel
import com.example.donotlate.feature.searchPlace.presentation.GooglePlacesModel
import com.example.donotlate.feature.searchPlace.presentation.LocationModel
import com.example.donotlate.feature.searchPlace.presentation.OpeningHoursModel
import com.example.donotlate.feature.searchPlace.presentation.PhotoModel
import com.example.donotlate.feature.searchPlace.presentation.ResultsModel

fun GooglePlacesEntity.toModel() = GooglePlacesModel(
    htmlAttributions, nextPageToken, results?.toModelResult(), status
)

fun List<ResultsEntity>.toModelResult(): List<ResultsModel> {

    return map {
        ResultsModel(
            it.formattedAddress,
            it.geometry?.toModel(),
            it.businessStatus,
            it.icon,
            it.iconMaskBaseUri,
            it.iconBackgroundColor,
            it.name,
            it.openingHours?.toModel(),
            it.photos?.map { it.toModel() },
            it.placeId,
            it.priceLevel,
            it.rating,
            it.types,
            it.userRatingsTotal,
            it.phoneNumber
        )
    }
}

fun ResultsEntity.toModel() = ResultsModel(
    formattedAddress,  geometry?.toModel(),businessStatus, icon, iconBackgroundColor, iconMaskBaseUri, name, openingHours?.toModel(),
    photos?.map { it.toModel() }, placeId, priceLevel, rating, types, userRatingsTotal, phoneNumber
)

fun List<PhotoEntity>.toModelPhoto(): List<PhotoModel> {
    return map {
        PhotoModel(
            it.height, it.htmlAttributions, it.photoReference, it.width
        )
    }
}


fun PhotoEntity.toModel() = PhotoModel(
    height, htmlAttributions, photoReference, width
)


fun GeometryEntity.toModel() = GeometryModel(
    location?.toModel()
)

fun LocationEntity.toModel() = LocationModel(
    lat, lng
)

fun OpeningHoursEntity.toModel() = OpeningHoursModel(
    openNow
)
