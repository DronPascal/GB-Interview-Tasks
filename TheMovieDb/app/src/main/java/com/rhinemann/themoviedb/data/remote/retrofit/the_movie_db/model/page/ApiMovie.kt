package com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId

data class ApiMovie(
    val id: MovieId,
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)