package com.example.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.data.model.GooglePlace
import com.example.donotlate.feature.searchPlace.data.model.SearchPlaces
import com.example.donotlate.feature.searchPlace.data.model.toEntity
import com.example.donotlate.feature.searchPlace.data.remote.GooglePlacesApiService
import com.example.donotlate.feature.searchPlace.domain.model.GooglePlacesEntity
import com.example.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity
import com.example.donotlate.feature.searchPlace.domain.model.toEntity
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


    override suspend fun searchPlacesList(
        query: String,
        language: String
    ) = googlePlacesApiService.requestSearchPlaces(query, language).toEntity()



}