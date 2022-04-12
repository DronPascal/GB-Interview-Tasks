package com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page

data class ApiMoviesPage(
    val page: Int,
    val results: List<ApiMovie>,
    val total_pages: Int,
    val total_results: Int
)