package com.nomorelateness.donotlate.feature.searchPlace.data.mapper

import com.nomorelateness.donotlate.feature.searchPlace.data.response.AuthorAttributions
import com.nomorelateness.donotlate.feature.searchPlace.data.response.Close
import com.nomorelateness.donotlate.feature.searchPlace.data.response.DisplayName
import com.nomorelateness.donotlate.feature.searchPlace.data.response.Open
import com.nomorelateness.donotlate.feature.searchPlace.data.response.Periods
import com.nomorelateness.donotlate.feature.searchPlace.data.response.Places
import com.nomorelateness.donotlate.feature.searchPlace.data.response.RegularOpeningHours
import com.nomorelateness.donotlate.feature.searchPlace.data.response.SearchLocation
import com.nomorelateness.donotlate.feature.searchPlace.data.response.SearchPhotos
import com.nomorelateness.donotlate.feature.searchPlace.data.response.SearchPlaces
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.AuthorAttributionsEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.CloseEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.DisplayNameEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.OpenEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.PeriodsEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.PlacesEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.RegularOpeningHoursEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.SearchLocationEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.SearchPhotosEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity


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