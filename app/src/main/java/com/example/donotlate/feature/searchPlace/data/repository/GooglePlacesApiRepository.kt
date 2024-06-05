package com.example.donotlate.feature.searchPlace.data.repository

import com.example.donotlate.feature.searchPlace.domain.model.GooglePlacesEntity
import retrofit2.http.Query

interface GooglePlacesRepository {


    //장소 유형 받아오기 : 동물원, 카페, 영화관, 음식점 ...
    suspend fun getPlaceTypeList(location:String, types: String): GooglePlacesEntity

    //영업 상태 받아오기
    suspend fun businessStatusList(location:String): GooglePlacesEntity

    //사용자 리뷰 수 받아오기
    suspend fun userRatingsTotalList(location:String): GooglePlacesEntity

    suspend fun searchList(
        query: String,
        radius: Int = 1500,
        apiKey: String = "AIzaSyAl7nz1KScbyyDNKUeYz4rrePkFZBDvhkc",
        language: String = "ko"
    ): GooglePlacesEntity


}