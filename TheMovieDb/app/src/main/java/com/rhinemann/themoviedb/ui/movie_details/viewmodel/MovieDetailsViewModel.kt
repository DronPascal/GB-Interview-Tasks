//package com.rhinemann.themoviedb.ui.movie_details.viewmodel
//
//import androidx.lifecycle.*
//import com.rhinemann.themoviedb.domain.interactors.GetMovieDetailed
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
///**
// * Created by dronpascal on 13.04.2022.
// */
//@HiltViewModel
//class MovieDetailsViewModel @Inject constructor(
//    private val getMovieDetailed: GetMovieDetailed,
//    savedStateHandle: SavedStateHandle,
//) : ViewModel() {
//
//    private val _state = MutableLiveData<MovieDetailsState>(MovieDetailsState.Loading(title = title))
//    val state: LiveData<MovieDetailsState> get() = _state
//
//    init {
//        viewModelScope.launch {
//}