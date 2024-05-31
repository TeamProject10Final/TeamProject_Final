package com.example.donotlate.searchPlace.service

import com.example.donotlate.searchPlace.searchplace.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApiService {

    @GET("/maps/api/place/nearbysearch/json")
    suspend fun requestSearch(
        @Query("key") apiKey: String = "AIzaSyCGjc9J745gjqjk79zbTsRC-pg90fXL6u0",
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String
    ): Result

}