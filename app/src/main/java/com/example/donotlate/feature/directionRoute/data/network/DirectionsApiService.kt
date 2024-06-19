package com.example.donotlate.feature.directionRoute.data.network

import com.example.donotlate.feature.directionRoute.data.model.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {

    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        //@Query("alternatives") alternatives: Boolean = false, //나중에 true로 바꾸기
        @Query("language") language: String = "ko",
//        @Query("key") apiKey: String = "AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8"
        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U"
    ): DirectionsResponse
    //출발시간 + 선호수단 + 선호방식
    @GET("directions/json")
    suspend fun getDirectionsWithDepartureTmRp(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("departure_time") departureTime: Int,
        @Query("transit_mode") transitMode: String,
        @Query("transit_routing_preference") transitRoutingPreference: String,
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
//        @Query("key") apiKey: String = "AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8"
        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U"
    ): DirectionsResponse


}