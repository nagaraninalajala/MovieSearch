package com.nagaraninalajala.moviesearch.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object to initialize Retrofit client
object RetrofitClient {
    private const val BASE_URL = "https://www.omdbapi.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Base URL for API requests
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
            .build()
            .create(ApiService::class.java) // Creates an implementation of ApiService
    }
}