package com.rhinemann.themoviedb.domain.interactors

import com.rhinemann.themoviedb.core.Result
import com.rhinemann.themoviedb.core.mapNestedSuccess
import com.rhinemann.themoviedb.core.mapSuccess
import com.rhinemann.themoviedb.data.local.core.MovieUrlProvider
import com.rhinemann.themoviedb.data.remote.MoviesDataSource
import com.rhinemann.themoviedb.data.remote.mappers.toEntity
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
import com.rhinemann.themoviedb.domain.models.Cast
import com.rhinemann.themoviedb.domain.models.Movie
import com.rhinemann.themoviedb.domain.models.MovieDetailed
import com.rhinemann.themoviedb.domain.models.MovieWithCast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by dronpascal on 13.04.2022.
 */
class GetMovieDetailed @Inject constructor(
    private val movieSource: MoviesDataSource,
    private val urlProvider: MovieUrlProvider,
) {

    suspend fun execute(id: MovieId): Flow<Result<MovieWithCast, Throwable>> =
        flow {
            val result = getMovieDetailsWithCast(id)
            if (currentCoroutineContext().isActive) {
                emit(result)
            }
        }

    private suspend fun getMovieDetailsWithCast(
        id: MovieId,
    ): Result<MovieWithCast, Throwable> =
        coroutineScope {
            val movieDetailedCall = async {
                movieSource.getMovieDetails(id)
                    .mapSuccess { entity -> entity.toEntity(urlProvider.baseImageUrl) }
            }
            val castCall = async {
                movieSource.getMovieCredits(id)
                    .mapSuccess { entity -> entity.cast.map { it.toEntity(urlProvider.baseImageUrl) } }

            }

            val movieDetailed = movieDetailedCall.await()
            val cast = castCall.await()

            movieDetailed.mapNestedSuccess { movie ->
                cast.mapSuccess { cast ->
                    MovieWithCast(
                        movie = movie,
                        cast = cast
                    )
                }
            }
        }

}

