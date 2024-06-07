package com.example.donotlate.feature.searchPlace.domain.model

import com.example.donotlate.feature.searchPlace.data.model.AuthorAttributions
import com.example.donotlate.feature.searchPlace.data.model.Close
import com.example.donotlate.feature.searchPlace.data.model.DisplayName
import com.example.donotlate.feature.searchPlace.data.model.Open
import com.example.donotlate.feature.searchPlace.data.model.Periods
import com.example.donotlate.feature.searchPlace.data.model.Places
import com.example.donotlate.feature.searchPlace.data.model.RegularOpeningHours
import com.example.donotlate.feature.searchPlace.data.model.SearchLocation
import com.example.donotlate.feature.searchPlace.data.model.SearchPhotos
import com.example.donotlate.feature.searchPlace.data.model.SearchPlaces


fun SearchPlaces.toEntity() = SearchPlacesEntity(
    places = places?.map { it.toEntity() }
)


fun Places.toEntity() = PlacesEntity(
    types = types,
    formattedAddress = formattedAddress,
    nationalPhoneNumber = nationalPhoneNumber,
    location = location?.toEntity(),
    rating = rating,
    regularOpeningHours = regularOpeningHours?.toEntity(),
    displayName = displayName?.toEntity(),
    photos = photos?.map { it.toEntity() }
)


fun SearchLocation.toEntity() = SearchLocationEntity(
    latitude = latitude,
    longitude = longitude
)


fun RegularOpeningHours.toEntity() = RegularOpeningHoursEntity(
    openNow = openNow,
    periods = periods?.map { it.toEntity() },
    weekdayDescriptions = weekdayDescriptions
)


fun Periods.toEntity() = PeriodsEntity(
    open = open?.toEntity(),
    close = close?.toEntity()
)


fun Open.toEntity() = OpenEntity(
    day = day,
    hour = hour,
    minute = minute
)


fun Close.toEntity() = CloseEntity(
    day = day,
    hour = hour,
    minute = minute
)


fun DisplayName.toEntity() = DisplayNameEntity(
    text = text,
    languageCode = languageCode
)


fun SearchPhotos.toEntity() = SearchPhotosEntity(
    name = name,
    widthPx = widthPx,
    heightPx = heightPx,
    authorAttributions = authorAttributions?.map { it.toEntity() }
)


fun AuthorAttributions.toEntity() = AuthorAttributionsEntity(
    displayName = displayName,
    uri = uri,
    photoUri = photoUri
)