package com.rhinemann.themoviedb.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.MoviesApi
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page.ApiMovie
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by dronpascal on 12.04.2022.
 */
class MoviesPagingSource(
    private val movieApi: MoviesApi,
    private val query: String
) : PagingSource<Int, ApiMovie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiMovie> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = if (query.isNotBlank()) {
                movieApi.searchMovie(query, position)
            } else {
                movieApi.getMoviePopular(position)
            }
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position.minus(1),
                nextKey = if (movies.isEmpty()) null else position.plus(1)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ApiMovie>): Int? {
        return null
    }

    companion object {
        private const val UNSPLASH_STARTING_PAGE_INDEX = 1
    }
}