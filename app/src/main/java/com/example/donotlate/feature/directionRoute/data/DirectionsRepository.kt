package com.example.donotlate.feature.directionRoute.data

import com.example.donotlate.feature.directionRoute.domain.DirectionsEntity

interface DirectionsRepository {
    suspend fun getDirections(origin: String, destination: String, mode: String): DirectionsEntity
}