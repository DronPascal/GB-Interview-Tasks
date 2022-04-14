package com.rhinemann.themoviedb.data.remote.mappers

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.credits.ApiCast
import com.rhinemann.themoviedb.domain.models.Cast

/**
 * Created by dronpascal on 14.04.2022.
 */
internal fun ApiCast.toEntity(baseImageUrl: String): Cast = Cast(
    id = this.id,
    name = this.name,
    character = this.character,
    imageUrl = getImageUrl(baseImageUrl, this.profile_path)
)