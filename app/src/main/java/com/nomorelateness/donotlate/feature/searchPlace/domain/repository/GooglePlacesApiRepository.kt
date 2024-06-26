package com.nomorelateness.donotlate.feature.searchPlace.domain.repository

import com.nomorelateness.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity

interface GooglePlacesApiRepository {

    suspend fun searchPlacesList(query: String, language: String, pageSize: Int): SearchPlacesEntity

}