package com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.detailed

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId

data class ApiMovieDetailed(
    val id: MovieId,
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: ApiBelongsToCollection,
    val budget: Int,
    val genres: List<ApiGenre>,
    val homepage: String,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ApiProductionCompany>,
    val production_countries: List<ApiProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<ApiSpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)