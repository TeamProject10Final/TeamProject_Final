package com.example.donotlate.feature.searchPlace.data.remote

import com.example.donotlate.feature.searchPlace.data.model.GooglePlace
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApiService {

    @GET("/maps/api/place/nearbysearch/json")
    suspend fun requestSearchWithType(

        @Query("location") location: String,
        //기기 gps 값 > 함수의 리턴값을 String으로 변환하여 입력
        @Query("type") types: String,
        @Query("key") apiKey: String = "AIzaSyCGjc9J745gjqjk79zbTsRC-pg90fXL6u0",
        @Query("radius") radius: Int = 1500

    ): GooglePlace

    @GET("/maps/api/place/nearbysearch/json")
    suspend fun requestSearch(

        @Query("location") location: String,
        @Query("key") apiKey: String = "AIzaSyCGjc9J745gjqjk79zbTsRC-pg90fXL6u0",
        @Query("radius") radius: Int = 1500

    ): GooglePlace

}