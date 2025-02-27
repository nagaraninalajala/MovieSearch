package com.nagaraninalajala.moviesearch.data.network

import com.nagaraninalajala.moviesearch.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query


// Retrofit API service interface for fetching movies
interface ApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,  // Movie search query
        @Query("apikey") apiKey: String = "8d6aa4ca" // API key for authentication
    ): MovieResponse
}