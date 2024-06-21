package com.example.donotlate.feature.directionRoute.data.network

import com.example.donotlate.feature.directionRoute.data.model.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {

    //세부사항 없이
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
//        @Query("key") apiKey: String = "AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8"
        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U"
    ): DirectionsResponse


    //도착시간
    @GET("directions/json")
    suspend fun getDirectionsArr(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("arrival_time") arrivalTime: Int,
        @Query("transit_mode") transitMode: String= "",
        @Query("transit_routing_preference") transitRoutingPreference: String= "",
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
//        @Query("key") apiKey: String = "AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8"
        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U"
    ): DirectionsResponse

    //출발시간
    @GET("directions/json")
    suspend fun getDirectionsDep(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("departure_time") departureTime: Int,
        @Query("transit_mode") transitMode: String ="",
        @Query("transit_routing_preference") transitRoutingPreference: String="",
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
        @Query("key") apiKey: String = "AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8"
//        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U"
    ): DirectionsResponse


    //시간 없이
    @GET("directions/json")
    suspend fun getDirectionsTmRp(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("transit_mode") transitMode: String ="",
        @Query("transit_routing_preference") transitRoutingPreference: String="",
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
        @Query("key") apiKey: String = "AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8"
//        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U"
    ): DirectionsResponse

}