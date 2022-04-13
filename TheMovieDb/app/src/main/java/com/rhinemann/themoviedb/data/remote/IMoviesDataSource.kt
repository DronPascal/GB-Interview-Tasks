package com.rhinemann.themoviedb.data.remote

import com.rhinemann.themoviedb.core.Result
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.credits.ApiMovieCredits
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.detailed.ApiMovieDetailed
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page.ApiMovie

/**
 * Created by dronpascal on 12.04.2022.
 */
interface IMoviesDataSource {

    suspend fun searchMovies(query: String): Result<List<ApiMovie>, Throwable>

    suspend fun getMoviePopular(page: Int): Result<List<ApiMovie>, Throwable>

    suspend fun getMovieDetails(id: MovieId): Result<ApiMovieDetailed, Throwable>

    suspend fun getMovieCredits(id: MovieId): Result<ApiMovieCredits, Throwable>

}