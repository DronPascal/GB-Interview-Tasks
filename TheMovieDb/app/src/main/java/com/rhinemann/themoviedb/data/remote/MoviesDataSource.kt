package com.rhinemann.themoviedb.data.remote


import com.rhinemann.themoviedb.core.Result
import com.rhinemann.themoviedb.core.doOnError
import com.rhinemann.themoviedb.core.runOperationCatching
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.MoviesApi
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.credits.ApiMovieCredits
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.detailed.ApiMovieDetailed
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page.ApiMovie
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by dronpascal on 12.04.2022.
 */
class MoviesDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) : IMoviesDataSource {

    override suspend fun searchMovies(query: String): Result<List<ApiMovie>, Throwable> =
        runOperationCatching { moviesApi.searchMovie(query).results }
            .doOnError { error -> Timber.e("Search movies from server error", error) }

    override suspend fun getMoviePopular(page: Int): Result<List<ApiMovie>, Throwable> =
        runOperationCatching { moviesApi.getMoviePopular(page).results }
            .doOnError { error -> Timber.e("Search popular movies from server error", error) }

    override suspend fun getMovieDetails(id: MovieId): Result<ApiMovieDetailed, Throwable> =
        runOperationCatching { moviesApi.getMovieDetails(id) }
            .doOnError { error -> Timber.e("getMovieDetails from server error", error) }


    override suspend fun getMovieCredits(id: MovieId): Result<ApiMovieCredits, Throwable> =
        runOperationCatching { moviesApi.getMovieCredits(id) }
            .doOnError { error -> Timber.e("getMovieCredits from server error", error) }
}