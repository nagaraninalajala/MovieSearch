package com.nagaraninalajala.moviesearch.data.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

// Data class to parse the API response
data class MovieResponse(
    @SerializedName("Search") val results: List<Movie>?  // List of movies returned from API
)

// Data class representing a single movie
@Immutable
data class Movie(
    @SerializedName("Title") val title: String,// Movie title
    @SerializedName("Year") val year: String,  // Release year
    @SerializedName("Poster") var poster: String // Poster URL
) {
    // Replaces "N/A" poster URL with a placeholder image
    init {
        if (poster == "N/A") {
            poster = "https://via.placeholder.com/150" // Dummy image URL
        }
    }
}