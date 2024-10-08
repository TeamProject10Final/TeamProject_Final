package com.nomorelateness.donotlate.feature.searchPlace.domain.usecase

import com.nomorelateness.donotlate.feature.searchPlace.domain.model.SearchPlacesEntity
import com.nomorelateness.donotlate.feature.searchPlace.domain.repository.GooglePlacesApiRepository

class GetSearchListUseCase(private val googlePlacesApiRepository: GooglePlacesApiRepository) {
    suspend operator fun invoke(query: String, language: String, pageSize: Int): SearchPlacesEntity {
        return googlePlacesApiRepository.searchPlacesList(query, language, pageSize)
    }
}