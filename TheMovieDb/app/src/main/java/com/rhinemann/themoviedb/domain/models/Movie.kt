package com.rhinemann.themoviedb.domain.models

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId

/**
 * Created by dronpascal on 12.04.2022.
 */
data class Movie(
    val id: MovieId,
    val name: String,
    val date: String,
    val rating: Int,
    val ratingColor: Int,
    val posterUrl: String
)
