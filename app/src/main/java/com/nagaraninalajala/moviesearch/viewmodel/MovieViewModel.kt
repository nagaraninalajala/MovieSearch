package com.nagaraninalajala.moviesearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nagaraninalajala.moviesearch.data.model.Movie
import com.nagaraninalajala.moviesearch.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()

    // StateFlow to hold the list of movies
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies


    /**
     * Fetches movies based on the search query.
     * If the query is blank, it exits early.
     * Clears previous search results before making an API request.
     * Ensures null movies are replaced with a default movie object.
     * Calls `onComplete` to stop any loading indicators.
     */
    fun searchMovies(query: String, onComplete: () -> Unit) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                _movies.value = emptyList() // Clear previous results
                val response = repository.searchMovies(query)

                // Process movie list and replace nulls with default values
                _movies.value = response?.results?.map { it.sanitize() ?: getDefaultMovie() } ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace() // Log the error in case of failure
            } finally {
                onComplete() // Stop loading spinner
            }
        }
    }

    /**
     * Extension function for the Movie class.
     * Ensures that:
     * - Title is not blank (sets "Unknown Title" if empty).
     * - Year is not blank (sets "N/A" if empty).
     * - Poster is valid (replaces "N/A" or blank URLs with a placeholder).
     */    private fun Movie.sanitize(): Movie {
        return this.copy(
            title = title.ifBlank { "Unknown Title" },
            year = year.ifBlank { "N/A" },
            poster = if (poster.isBlank() || poster == "N/A") "https://via.placeholder.com/150" else poster
        )
    }


    /**
     * Provides a default movie object in case the API returns a null movie.
     */
    private fun getDefaultMovie(): Movie {
        return Movie(
            title = "Unknown Title",
            year = "N/A",
            poster = "https://via.placeholder.com/150"
        )
    }

}