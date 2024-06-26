package com.nomorelateness.donotlate.feature.searchPlace.api

import com.nomorelateness.donotlate.feature.searchPlace.data.remote.GooglePlacesApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkClient {

//    private const val GOOGLE_BASE_URL = "https://maps.googleapis.com"
    private const val SEARCH_BASE_URL = "https://places.googleapis.com"

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

//    private val googleRetrofit = Retrofit.Builder()
//        .baseUrl(GOOGLE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
//            createOkHttpClient()
//        ).build()
//
//    val googleNetWork: GooglePlacesApiService = googleRetrofit.create(GooglePlacesApiService::class.java)

    private val searchRetrofit = Retrofit.Builder()
        .baseUrl(SEARCH_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
            createOkHttpClient()
        ).build()

    val searchNetWork : GooglePlacesApiService = searchRetrofit.create(GooglePlacesApiService::class.java)
}