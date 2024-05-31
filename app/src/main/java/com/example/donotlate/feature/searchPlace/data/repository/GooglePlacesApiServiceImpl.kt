package com.example.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.data.remote.GooglePlacesApiService
import com.example.donotlate.feature.searchPlace.domain.repository.GooglePlacesRepository

class GooglePlacesApiServiceImpl (
    private val googlePlacesApiService: GooglePlacesApiService
) : GooglePlacesRepository {
    override suspend fun getMapInfo() {

    }

}