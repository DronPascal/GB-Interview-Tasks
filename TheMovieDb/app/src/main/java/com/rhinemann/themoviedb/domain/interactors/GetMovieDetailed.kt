//package com.rhinemann.themoviedb.domain.interactors
//
//import com.rhinemann.themoviedb.core.Result
//import com.rhinemann.themoviedb.core.mapNestedSuccess
//import com.rhinemann.themoviedb.core.mapSuccess
//import com.rhinemann.themoviedb.data.local.core.MovieUrlProvider
//import com.rhinemann.themoviedb.data.remote.MoviesDataSource
//import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.MovieId
//import com.rhinemann.themoviedb.domain.models.Movie
//import com.rhinemann.themoviedb.domain.models.MovieDetailed
//import kotlinx.coroutines.async
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import javax.inject.Inject
//
///**
// * Created by dronpascal on 13.04.2022.
// */
//class GetMovieDetailed @Inject constructor(
//    private val movieSource: MoviesDataSource,
//    private val urlProvider: MovieUrlProvider,
//) {
//
//    suspend fun execute(id: MovieId): Flow<Result<Movie, Throwable>> =
//        flow {
//            val detailed = movieSource.getMovieDetails(id)
//            val credits = movieSource.getMovieCredits(id)
//        }
//
//    private suspend fun getMovieDetailsWithReviews(
//        id: MovieId,
//    ): Result<Pair<MovieDetailed, List<Credits>>, Throwable> =
//        coroutineScope {
//            val detailedCall = async { movieSource.getMovieDetails(id) }
//            val creditsCall = async { movieSource.getMovieCredits(id) }
//
//            val details = detailedCall.await()
//            val credits = creditsCall.await()
//
//            details.mapNestedSuccess { movie ->
//                credits.mapSuccess { allReviews -> movie to allReviews }
//            }
//        }
//}
//
