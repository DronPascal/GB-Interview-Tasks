package com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db

import com.rhinemann.themoviedb.data.local.core.MovieUrlProvider
import com.rhinemann.themoviedb.data.remote.retrofit.QueryInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by dronpascal on 12.04.2022.
 */
class MovieHttpClient @Inject constructor(
    movieUrlProvider: MovieUrlProvider,
) : IMovieHttpClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor(QueryInterceptor(hashMapOf("api_key" to movieUrlProvider.apiKey)))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(movieUrlProvider.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val moviesApi: MoviesApi by lazy(LazyThreadSafetyMode.NONE) { retrofit.create(MoviesApi::class.java) }
}