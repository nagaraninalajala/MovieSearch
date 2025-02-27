package com.nagaraninalajala.moviesearch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import com.nagaraninalajala.moviesearch.R
import com.nagaraninalajala.moviesearch.data.model.Movie
import com.nagaraninalajala.moviesearch.viewmodel.MovieViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen() {
    val movieViewModel: MovieViewModel = viewModel()
    val movies by movieViewModel.movies.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movie Search") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search for a movie") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Search Button
            Button(
                onClick = {
                    if (searchQuery.isNotBlank()) {
                        isLoading = true
                        movieViewModel.searchMovies(searchQuery) {
                            isLoading = false
                        }
                    }
                },
                enabled = searchQuery.isNotBlank(), // Disable if query is empty
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {
                Text("Search")
            }

            // Show Loading Indicator
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            // Show Movies List
            // Show Movies List
            if (movies.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    items(movies) { movie ->
                        MovieItem(movie)
                    }
                }
            }
            else if (!isLoading && searchQuery.isNotBlank()) {
                // Show No Movies Found Message
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_results), // Placeholder Image
                        contentDescription = "No Results",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "No movies found. Try a different search!",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = movie.poster,
                    imageLoader = imageLoader // Pass the imageLoader correctly
                ),
                contentDescription = "Movie Poster", //  Move contentDescription here
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop
            )

            // Movie Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                Text(
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )
            }
        }
    }
}