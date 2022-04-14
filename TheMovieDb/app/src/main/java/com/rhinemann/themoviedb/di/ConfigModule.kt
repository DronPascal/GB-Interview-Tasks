package com.rhinemann.themoviedb.di

import com.rhinemann.themoviedb.BuildConfig
import com.rhinemann.themoviedb.data.local.core.BuildConfigProvider
import com.rhinemann.themoviedb.data.local.core.MovieUrlProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by dronpascal on 12.04.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object ConfigsModule {

    @Provides
    fun provideBuildConfigProvider(): BuildConfigProvider =
        BuildConfigProvider(
            debug = BuildConfig.DEBUG,
            appId = BuildConfig.APPLICATION_ID,
            buildType = BuildConfig.BUILD_TYPE,
            flavor = BuildConfig.FLAVOR,
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME
        )

    @Provides
    fun provideMovieUrlProvider(): MovieUrlProvider =
        MovieUrlProvider(
            baseUrl = BuildConfig.BASE_URL,
            apiKey = BuildConfig.THE_MOVIE_DB_API_KEY,
            baseImageUrl = "https://image.tmdb.org/t/p/w300",
            browseMovieBaseUrl = "https://www.themoviedb.org/movie/"
        )
}