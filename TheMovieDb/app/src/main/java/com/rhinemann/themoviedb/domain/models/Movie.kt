package com.rhinemann.themoviedb.domain.models

import android.os.Parcelable
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.StringBufferInputStream

/**
 * Created by dronpascal on 12.04.2022.
 */
@Parcelize
data class Movie(
    val id: @RawValue MovieId,
    val name: String,
    val date: String,
    val overview: String,
    val rating: Int,
    val ratingColorHex: String,
    val posterUrl: String,
    val backgroundUrl: String,
) : Parcelable
