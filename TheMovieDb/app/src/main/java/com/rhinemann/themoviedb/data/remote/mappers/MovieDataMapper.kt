package com.rhinemann.themoviedb.data.remote.mappers

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page.ApiMovie
import com.rhinemann.themoviedb.domain.models.Movie
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by dronpascal on 12.04.2022.
 */

internal fun ApiMovie.toEntity(baseImageUrl: String): Movie = Movie(
    id = this.id,
    name = this.title,
    date = getFormatDate(this.release_date),
    rating = getRating100(this.vote_average),
    ratingColor = 0,
    posterUrl = getPosterUrl(baseImageUrl, this.poster_path),
)

private fun getPosterUrl(baseImageUrl: String, posterPath: String?): String =
    "${baseImageUrl}${posterPath}"

private fun getFormatDate(text: String): String {
    val parsedDate = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return parsedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

private fun getRating100(vote_average: Double): Int {
    if (vote_average > 10) return 100
    if (vote_average < 0) return 0
    return (vote_average * 10).toInt()
}

private fun getColorFromRating(percentage: Int) {
}
