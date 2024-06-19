package com.example.finaldirectionexample01.domain.usecase

import com.example.donotlate.feature.directionRoute.domain.DirectionsRepository

class GetDirWithTmRpUseCase
constructor(private val repository: DirectionsRepository) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        transitMode: String,
        transitRoutingPreference: String
    ) = repository.getDirectionsWithTmRp(origin, destination, transitMode, transitRoutingPreference)
}