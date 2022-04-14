package com.rhinemann.themoviedb.domain.models

/**
 * Created by dronpascal on 14.04.2022.
 */
data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val imageUrl: String
)