package com.example.donotlate.feature.directionRoute.domain

interface DirectionsRepository {
    suspend fun getDirections(origin: String, destination: String, mode: String): DirectionsEntity

    suspend fun getDirectionsWithDepartureTmRp(origin: String, destination: String, departureTime: Int, transitMode: String, transitRoutingPreference: String): DirectionsEntity

    suspend fun getDirectionsWithTmRp(origin: String, destination: String, transitMode: String, transitRoutingPreference: String): DirectionsEntity

    suspend fun getDirectionsWithArrivalTmRp(origin: String, destination: String, arrivalTime: Int, transitMode: String, transitRoutingPreference: String): DirectionsEntity
}