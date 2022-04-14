package com.rhinemann.themoviedb.domain.models

/**
 * Created by dronpascal on 14.04.2022.
 */
data class MovieWithCast(
    val movie: MovieDetailed,
    val cast: List<Cast>,
)