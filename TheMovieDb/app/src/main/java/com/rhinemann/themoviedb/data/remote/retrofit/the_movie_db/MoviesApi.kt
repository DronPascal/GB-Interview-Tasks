package com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.credits.ApiMovieCredits
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.detailed.ApiMovieDetailed
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page.ApiMoviesPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by dronpascal on 12.04.2022.
 */
interface MoviesApi {

//    @GET("search/movie")
//    suspend fun getMoviePagingSource(
//        @Query("query") query: String,
//        @Query("page") page: Int = 1,
//    ): PagingSource<Int, ApiMoviesPage>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
    ): ApiMoviesPage

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: MovieId,
    ): ApiMovieDetailed

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") id: MovieId,
    ): ApiMovieCredits
}