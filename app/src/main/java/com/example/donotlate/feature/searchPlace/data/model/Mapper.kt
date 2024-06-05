package com.example.donotlate.feature.searchPlace.data.model

import com.example.donotlate.feature.searchPlace.domain.model.GeometryEntity
import com.example.donotlate.feature.searchPlace.domain.model.GooglePlacesEntity
import com.example.donotlate.feature.searchPlace.domain.model.LocationEntity
import com.example.donotlate.feature.searchPlace.domain.model.OpeningHoursEntity
import com.example.donotlate.feature.searchPlace.domain.model.PhotoEntity
import com.example.donotlate.feature.searchPlace.domain.model.ResultsEntity

fun GooglePlace.toEntity(): GooglePlacesEntity {
    return GooglePlacesEntity(htmlAttributions, nextPageToken, results?.map { place -> place.toEntity() } , status)
}

fun results.toEntity(): ResultsEntity {
    return ResultsEntity(formattedAddress, geometry?.toEntity(), businessStatus, icon, iconBackgroundColor, iconMaskBaseUri, name, openingHours?.toEntity(), photos?.map { it.toEntity() } , placeId, priceLevel, rating, types, userRatingsTotal, phoneNumber)
}

fun Photo.toEntity() : PhotoEntity {
    return PhotoEntity(height, htmlAttributions, photoReference, width)
}

fun Geometry.toEntity() : GeometryEntity {
    return GeometryEntity(location?.toEntity())
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(lat, lng)
}

fun OpeningHours.toEntity(): OpeningHoursEntity {
    return OpeningHoursEntity(openNow)
}