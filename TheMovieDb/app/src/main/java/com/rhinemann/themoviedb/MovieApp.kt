package com.rhinemann.themoviedb

import android.app.Application
import com.rhinemann.themoviedb.data.local.core.BuildConfigProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by dronpascal on 11.04.2022.
 */
@HiltAndroidApp
class MovieApp : Application() {

    @Inject
    lateinit var buildConfig: BuildConfigProvider

    override fun onCreate() {
        super.onCreate()
        if (buildConfig.debug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}