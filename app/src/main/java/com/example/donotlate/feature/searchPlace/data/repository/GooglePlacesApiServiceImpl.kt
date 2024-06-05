package com.example.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.data.model.GooglePlace
import com.example.donotlate.feature.searchPlace.data.model.toEntity
import com.example.donotlate.feature.searchPlace.data.remote.GooglePlacesApiService
import com.example.donotlate.feature.searchPlace.domain.model.GooglePlacesEntity
import retrofit2.http.Query

class GooglePlacesRepositoryImpl (
    private val googlePlacesApiService: GooglePlacesApiService
) : GooglePlacesRepository {
    override suspend fun getPlaceTypeList(location: String, types: String): GooglePlacesEntity {
        return googlePlacesApiService.requestSearchWithType(location, types).toEntity()
    }

    override suspend fun businessStatusList(location: String): GooglePlacesEntity {
        return googlePlacesApiService.requestSearch(location).toEntity()
    }

    override suspend fun userRatingsTotalList(location: String): GooglePlacesEntity {
        return googlePlacesApiService.requestSearch(location).toEntity()
    }

    override suspend fun searchList(
        query: String,
        radius: Int,
        apiKey: String,
        language: String
    ) = googlePlacesApiService.requestDestination(query, radius, apiKey, language).toEntity()


}