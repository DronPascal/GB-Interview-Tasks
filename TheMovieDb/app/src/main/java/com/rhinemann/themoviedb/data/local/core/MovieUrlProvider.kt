package com.rhinemann.themoviedb.data.local.core

/**
 * Created by dronpascal on 12.04.2022.
 */
data class MovieUrlProvider(
    val baseUrl: String,
    val apiKey: String,
    val baseImageUrl: String,
    val browseMovieBaseUrl: String,
)