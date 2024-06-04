package com.example.donotlate.feature.searchPlace.domain.model

import com.example.donotlate.feature.searchPlace.presentation.GeometryModel
import com.example.donotlate.feature.searchPlace.presentation.GooglePlacesModel
import com.example.donotlate.feature.searchPlace.presentation.LocationModel
import com.example.donotlate.feature.searchPlace.presentation.OpeningHoursModel
import com.example.donotlate.feature.searchPlace.presentation.PhotoModel
import com.example.donotlate.feature.searchPlace.presentation.ResultModel

fun GooglePlacesEntity.toModel() = GooglePlacesModel(
    htmlAttributions, nextPageToken, results.toModelResult(), status
)

fun List<ResultEntity>.toModelResult(): List<ResultModel> {

    return map {
        ResultModel(
            it.businessStatus,
            it.icon,
            it.iconMaskBaseUri,
            it.iconBackgroundColor,
            it.name,
            it.openingHours.toModel(),
            it.photos.toModelPhoto(),
            it.placeId,
            it.priceLevel,
            it.rating,
            it.types,
            it.userRatingsTotal
        )
    }
}

fun ResultEntity.toModel() = ResultModel(
    businessStatus, icon, iconBackgroundColor, iconMaskBaseUri, name, openingHours.toModel(),
    photos.toModelPhoto(), placeId, priceLevel, rating, types, userRatingsTotal
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
    location.toModel()
)

fun LocationEntity.toModel() = LocationModel(
    lat, lng
)

fun OpeningHoursEntity.toModel() = OpeningHoursModel(
    openNow
)
