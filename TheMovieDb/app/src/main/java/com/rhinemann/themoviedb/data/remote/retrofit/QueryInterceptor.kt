package com.rhinemann.themoviedb.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by dronpascal on 12.04.2022.
 */
class QueryInterceptor(private val args: HashMap<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val url = originalRequest.url.newBuilder().apply {
            args.forEach { (key, value) -> addQueryParameter(key, value) }
        }.build()

        return chain.proceed(
            originalRequest.newBuilder()
                .url(url)
                .build()
        )
    }
}