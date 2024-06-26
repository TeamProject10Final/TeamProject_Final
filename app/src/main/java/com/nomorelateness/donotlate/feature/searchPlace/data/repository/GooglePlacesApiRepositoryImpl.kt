package com.nomorelateness.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.data.mapper.toEntity
import com.example.donotlate.feature.searchPlace.data.remote.GooglePlacesApiService
import com.example.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity
import com.example.donotlate.feature.searchPlace.domain.repository.GooglePlacesApiRepository

class GooglePlacesApiRepositoryImpl (
    private val googlePlacesApiService: GooglePlacesApiService
) : GooglePlacesApiRepository {

    override suspend fun searchPlacesList(
        query: String,
        language: String,
        pageSize: Int
    ): SearchPlacesEntity = googlePlacesApiService.requestSearchPlaces(query, language, pageSize).toEntity()


}