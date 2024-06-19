package com.example.donotlate.feature.directionRoute.domain

interface DirectionsRepository {
    suspend fun getDirections(origin: String, destination: String, mode: String): DirectionsEntity
}