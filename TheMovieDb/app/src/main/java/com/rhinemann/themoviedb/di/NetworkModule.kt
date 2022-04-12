package com.rhinemann.themoviedb.di

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.IMovieHttpClient
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.MovieHttpClient
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.MoviesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by dronpascal on 12.04.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindMovieDbClient(
        impl: MovieHttpClient,
    ): IMovieHttpClient
}

@Module
@InstallIn(SingletonComponent::class)
object ApiWrapperModule {

    @Provides
    @Singleton
    fun provideMoviesApi(client: IMovieHttpClient): MoviesApi = client.moviesApi
}
