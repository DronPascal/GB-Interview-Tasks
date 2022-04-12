package com.rhinemann.themoviedb.data.local.core

/**
 * Created by dronpascal on 12.04.2022.
 */
data class BuildConfigProvider(
    val debug: Boolean,
    val appId: String,
    val buildType: String,
    val flavor: String,
    val versionCode: Int,
    val versionName: String,
)