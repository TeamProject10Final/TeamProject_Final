package com.example.donotlate.feature.searchPlace.data.remote

import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.model.GooglePlace
import com.example.donotlate.feature.searchPlace.data.model.SearchPlaces
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GooglePlacesApiService {

    @GET("/maps/api/place/nearbysearch/json")
    suspend fun requestSearchWithType(

        @Query("location") location: String,
        //기기 gps 값 > 함수의 리턴값을 String으로 변환하여 입력
        @Query("type") types: String,
        //restaurant, cafe, movie_theater, park, shopping_mall
        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U",
        @Query("radius") radius: Int = 1500,
        @Query("language") language: String = "ko"

    ): GooglePlace

    @GET("/maps/api/place/nearbysearch/json")
    suspend fun requestSearch(

        @Query("location") location: String,
        @Query("key") apiKey: String = "AIzaSyBqe8TQyjF1ndxlzGoZ6GYiWokc8Mi-77U",
        @Query("radius") radius: Int = 1500,
        @Query("language") language: String = "ko"

    ): GooglePlace


    @POST("/v1/places:searchText")
    suspend fun requestSearchPlaces(
        @Query("textQuery") textQuery: String,
        @Query("languageCode") language: String = "ko",
        @Header("Content-Type") contentType: String = "application/json",
        @Header("X-Goog-Api-Key") apiKey: String = NetWorkClient.API_KEY,
        @Header("X-Goog-FieldMask") displayName: String = "places.displayName",
        @Header("X-Goog-FieldMask") address: String = "places.formattedAddress",
        @Header("X-Goog-FieldMask") photos: String = "places.photos",
        @Header("X-Goog-FieldMask") types: String = "places.types",
        @Header("X-Goog-FieldMask") rating: String = "places.rating",
        @Header("X-Goog-FieldMask") location: String = "places.location",
        @Header("X-Goog-FieldMask") hours: String = "places.regularOpeningHours",
        @Header("X-Goog-FieldMask") phoneNumber: String = "places.nationalPhoneNumber",
    ): SearchPlaces


    //    @Headers(
//        "Content-Type: application/json",
//        "X-Goog-Api-Key: AIzaSyAl7nz1KScbyyDNKUeYz4rrePkFZBDvhkc",
//        "X-Goog-FieldMask: places.displayName",
//        "X-Goog-FieldMask: places.formattedAddress",
//        "X-Goog-FieldMask: places.photos",
//        "X-Goog-FieldMask: places.types",
//        "X-Goog-FieldMask: places.rating",
//        "X-Goog-FieldMask: places.location",
//        "X-Goog-FieldMask: places.places.regularOpeningHours"
//        )
}
