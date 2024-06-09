package com.example.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity

interface GooglePlacesRepository {

    suspend fun clickChipList(
        types: String,
        language: String = "ko"
    ): SearchPlacesEntity

    suspend fun searchPlacesList(
        query: String,
        language : String = "ko"
    ): SearchPlacesEntity

}