package com.nomorelateness.donotlate.feature.directionRoute.domain.usecase

import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsRepository

class GetDirWithArrTmRpUseCase
constructor(private val repository: DirectionsRepository) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        arrivalTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ) = repository.getDirectionsWithArrivalTmRp(
        origin,
        destination,
        arrivalTime,
        transitMode,
        transitRoutingPreference
    )
}