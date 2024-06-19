package com.example.donotlate.feature.directionRoute.domain

interface DirectionsRepository {
    suspend fun getDirections(origin: String, destination: String, mode: String): DirectionsEntity

    suspend fun getDirectionsWithDepartureTmRp(origin: String, destination: String, departureTime: Int, transitMode: String, transitRoutingPreference: String): DirectionsEntity
}