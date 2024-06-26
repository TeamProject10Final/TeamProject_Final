package com.nomorelateness.donotlate.feature.directionRoute.domain.usecase

import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsRepository

class GetDirWithDepTmRpUseCase
constructor(private val repository: DirectionsRepository) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        departureTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ) = repository.getDirectionsWithDepartureTmRp(
        origin,
        destination,
        departureTime,
        transitMode,
        transitRoutingPreference
    )
}