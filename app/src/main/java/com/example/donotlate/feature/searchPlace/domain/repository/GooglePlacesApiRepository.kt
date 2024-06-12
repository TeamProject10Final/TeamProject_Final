package com.example.donotlate.feature.searchPlace.domain.repository

import com.example.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity

interface GooglePlacesApiRepository {

    suspend fun searchPlacesList(query: String, language: String, pageSize: Int): SearchPlacesEntity

}