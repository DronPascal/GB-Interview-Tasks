package com.rhinemann.themoviedb.ui.movies.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.rhinemann.themoviedb.domain.interactors.GetMovieSearchFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMovieFlow: GetMovieSearchFlow,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentQuery = savedStateHandle.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val movies = currentQuery.switchMap { queryString ->
        getMovieFlow.execute(queryString).asLiveData().cachedIn(viewModelScope)
    }

    fun searchMovies(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = ""
    }
}