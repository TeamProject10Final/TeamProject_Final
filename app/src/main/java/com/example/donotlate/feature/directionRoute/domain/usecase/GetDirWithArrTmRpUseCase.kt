package com.example.finaldirectionexample01.domain.usecase

import com.example.donotlate.feature.directionRoute.domain.DirectionsRepository

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