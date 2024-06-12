package com.example.donotlate.feature.searchPlace.domain.usecase

import com.example.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity
import com.example.donotlate.feature.searchPlace.domain.repository.GooglePlacesApiRepository
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel

class GetSearchListUseCase(private val googlePlacesApiRepository: GooglePlacesApiRepository) {
    suspend operator fun invoke(query: String, language: String, pageSize: Int): SearchPlacesEntity {
        return googlePlacesApiRepository.searchPlacesList(query, language, pageSize)
    }
}