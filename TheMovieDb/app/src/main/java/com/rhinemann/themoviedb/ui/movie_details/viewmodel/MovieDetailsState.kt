package com.rhinemann.themoviedb.ui.movie_details.viewmodel

import com.rhinemann.themoviedb.domain.models.MovieWithCast

/**
 * Created by dronpascal on 13.04.2022.
 */
sealed class MovieDetailsState {

    data class Loading(val title: String) : MovieDetailsState()

    data class Result(
        val movie: MovieWithCast,
    ) : MovieDetailsState()

    object Error : MovieDetailsState()
}