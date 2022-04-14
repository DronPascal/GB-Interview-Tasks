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
                pageSize = TMD_API_PAGE_SIZE,
                maxSize = MAX_CACHED_PAGES * TMD_API_PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = PREFETCH_PAGES_NUMBER * TMD_API_PAGE_SIZE,
                initialLoadSize = INIT_LOAD_PAGES_NUMBER * TMD_API_PAGE_SIZE
            ),
            pagingSourceFactory = { MoviesPagingSource(movieApi, query) }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toEntity(urlProvider.baseImageUrl) }
            }

    private companion object {
        const val TMD_API_PAGE_SIZE = 20
        const val MAX_CACHED_PAGES = 12
        const val PREFETCH_PAGES_NUMBER = 5
        const val INIT_LOAD_PAGES_NUMBER = 5
    }
}