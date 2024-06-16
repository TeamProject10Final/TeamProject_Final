package com.example.donotlate.feature.directionRoute.data

import android.util.Log
import com.example.donotlate.feature.directionRoute.data.network.DirectionsApiService
import com.example.donotlate.feature.directionRoute.domain.DirectionsEntity

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
}