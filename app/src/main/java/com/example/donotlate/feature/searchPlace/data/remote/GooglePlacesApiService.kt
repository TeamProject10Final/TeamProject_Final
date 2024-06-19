package com.example.donotlate.feature.searchPlace.data.remote

import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.response.SearchPlaces
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GooglePlacesApiService {


    @POST("/v1/places:searchText")
    suspend fun requestSearchPlaces(
        @Query("textQuery") textQuery: String,
        @Query("languageCode") language: String = "ko",
        @Query("pageSize") pageSize: Int,
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
}

