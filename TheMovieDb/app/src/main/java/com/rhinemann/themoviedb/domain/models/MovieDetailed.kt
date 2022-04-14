package com.rhinemann.themoviedb.domain.models

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId

/**
 * Created by dronpascal on 12.04.2022.
 */
data class MovieDetailed(
    val id: MovieId,
    val name: String,
    val overview: String,
    val date: String,
    val runtime: String,
    val genres: List<String>,
    val rating: Int,
    val ratingColorHex: String,
    val posterUrl: String,
    val backgroundUrl: String,
)