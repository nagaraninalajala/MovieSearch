package com.nagaraninalajala.moviesearch.data.repository

import android.util.Log
import com.nagaraninalajala.moviesearch.data.model.Movie
import com.nagaraninalajala.moviesearch.data.model.MovieResponse
import com.nagaraninalajala.moviesearch.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Repository class to handle API calls
class MovieRepository {

        private val apiService = RetrofitClient.apiService

    /**
     * Fetches movies from the API based on the search query.
     * Logs response for debugging and handles errors gracefully.
     */
        suspend fun searchMovies(query: String): MovieResponse? {
            return try {
                val response = apiService.searchMovies(query)
                Log.d("API_RESPONSE", "Response: $response") // Debugging
                response
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                null // Return null to prevent crashes
            }
        }
}