package com.example.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.data.remote.GooglePlacesApiService
import com.example.donotlate.feature.searchPlace.domain.model.toEntity

class GooglePlacesRepositoryImpl (
    private val googlePlacesApiService: GooglePlacesApiService
) : GooglePlacesRepository {

    override suspend fun searchPlacesList(
        query: String,
        language: String
    ) = googlePlacesApiService.requestSearchPlaces(query, language).toEntity()


}