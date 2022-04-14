package com.rhinemann.themoviedb.ui.movie_details.viewmodel

import androidx.lifecycle.*
import com.rhinemann.themoviedb.core.Result
import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
import com.rhinemann.themoviedb.domain.interactors.GetMovieDetailed
import com.rhinemann.themoviedb.domain.models.Cast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by dronpascal on 13.04.2022.
 */
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailed: GetMovieDetailed,
    @Suppress("UNUSED_PARAMETER") savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state =
        MutableLiveData<MovieDetailsState>(MovieDetailsState.Loading(title = "Loading"))
    val state: LiveData<MovieDetailsState> get() = _state

    fun onMovieIdReady(id: MovieId) {
        loadMovieDetailed(id)
    }

    private fun loadMovieDetailed(id: MovieId) {
        getMovieDetailed.execute(id).onEach { detailsResult ->
            when (detailsResult) {
                is Result.Success -> _state.value =
                    MovieDetailsState.Result(detailsResult.result)
                is Result.Error -> MovieDetailsState.Error
            }
        }.launchIn(viewModelScope)
    }

    fun onItemClick(@Suppress("UNUSED_PARAMETER") cast: Cast) {
    }
}














