package com.example.donotlate.feature.directionRoute.domain.usecase

import com.example.donotlate.feature.directionRoute.data.DirectionsRepository

class GetDirectionsUseCase
constructor(private val repository: DirectionsRepository){
    suspend operator fun invoke(origin: String, destination: String, mode: String) = repository.getDirections(origin, destination, mode)
}