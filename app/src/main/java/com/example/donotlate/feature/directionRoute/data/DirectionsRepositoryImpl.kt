package com.example.donotlate.feature.directionRoute.data

import android.util.Log
import com.example.donotlate.feature.directionRoute.data.network.DirectionsApiService
import com.example.donotlate.feature.directionRoute.domain.DirectionsEntity
import com.example.donotlate.feature.directionRoute.domain.DirectionsRepository

class DirectionsRepositoryImpl(
    private val apiService: DirectionsApiService
) : DirectionsRepository {

    override suspend fun getDirections(
        origin: String,
        destination: String,
        mode: String,
    ): DirectionsEntity {
        val result = apiService.getDirections(origin, destination, mode).toEntity()
        Log.d("확인", "impl: $result")
        return result
    }

    override suspend fun getDirectionsWithDepartureTmRp(
        origin: String,
        destination: String,
        departureTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ): DirectionsEntity {
        val result = apiService.getDirectionsWithDepartureTmRp(origin, destination, departureTime, transitMode, transitRoutingPreference).toEntity()
        Log.d("확인", "impl 8: $result")
        return result
    }
}