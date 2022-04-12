package com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.credits

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId

data class ApiMovieCredits(
    val id: MovieId,
    val cast: List<ApiCast>,
    val crew: List<ApiCrew>
)