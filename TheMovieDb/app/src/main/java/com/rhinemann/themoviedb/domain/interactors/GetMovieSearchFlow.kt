package com.rhinemann.themoviedb.domain.interactors

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rhinemann.themoviedb.data.local.core.MovieUrlProvider
import com.rhinemann.themoviedb.data.remote.MoviesPagingSource
import com.rhinemann.themoviedb.data.remote.mappers.toEntity
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.MoviesApi
import com.rhinemann.themoviedb.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by dronpascal on 12.04.2022.
 */
class GetMovieSearchFlow @Inject constructor(
    private val movieApi: MoviesApi,
    private val urlProvider: MovieUrlProvider,
) {

    fun execute(query: String): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = false,
                prefetchDistance = 20,
                initialLoadSize = 40
            ),
            pagingSourceFactory = { MoviesPagingSource(movieApi, query) }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toEntity(urlProvider.baseImageUrl) }
            }
}