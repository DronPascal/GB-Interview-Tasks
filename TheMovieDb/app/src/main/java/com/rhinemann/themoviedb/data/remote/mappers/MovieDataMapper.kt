package com.rhinemann.themoviedb.data.remote.mappers

import com.rhinemann.themoviedb.data.remote.retrofit.the_movie_db.model.page.ApiMovie
import com.rhinemann.themoviedb.domain.models.Color
import com.rhinemann.themoviedb.domain.models.Movie
import okhttp3.internal.toHexString
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.absoluteValue
import kotlin.math.floor

/**
 * Created by dronpascal on 12.04.2022.
 */

internal fun ApiMovie.toEntity(baseImageUrl: String): Movie = Movie(
    id = this.id,
    name = this.title.also { println(it) },
    date = getFormatDate(this.release_date ?: ""),
    overview = this.overview,
    rating = getRating100(this.vote_average).also { println("rating $it") },
    ratingColorHex = getColorFromRating(getRating100(this.vote_average)),
    posterUrl = getImageUrl(baseImageUrl, this.poster_path),
    backgroundUrl = getImageUrl(baseImageUrl, this.backdrop_path)
)

private fun getImageUrl(baseImageUrl: String, posterPath: String?): String =
    "${baseImageUrl}${posterPath}"

private fun getFormatDate(text: String): String {
    if (text.isBlank() || text.isEmpty()) return ""
    val parsedDate = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return parsedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

private fun getRating100(vote_average: Double): Int {
    if (vote_average > 10) return 100
    if (vote_average < 0) return 0
    return (vote_average * 10).toInt()
}

private fun getColorFromRating(percentage: Int): String {
    return colorGradient(
        percentage,
        Color(255, 0, 0),
        Color(255, 255, 0),
        Color(0, 255, 0)
    )
}

private fun colorGradient(
    fadeFraction: Int,
    rgbColor1: Color,
    rgbColor2: Color,
    rgbColor3: Color?
): String {
    var color1 = rgbColor1
    var color2 = rgbColor2
    var fade = fadeFraction.toDouble() / 100
    // Do we have 3 colors for the gradient? Need to adjust the params.
    if (rgbColor3 != null) {
        fade *= 2

        // Find which interval to use and adjust the fade percentage
        if (fade >= 1) {
            fade -= 1
            color1 = rgbColor2
            color2 = rgbColor3
        }
    }

    val diffRed = color2.red - color1.red
    val diffGreen = color2.green - color1.green
    val diffBlue = color2.blue - color1.blue

    val gradient = Color(
        floor((color1.red + (diffRed * fade)).toDouble()).toInt().absoluteValue,
        floor((color1.green + (diffGreen * fade)).toDouble()).toInt().absoluteValue,
        floor((color1.blue + (diffBlue * fade)).toDouble()).toInt().absoluteValue,
    )

    var result = "#"
    listOf(
        gradient.red.toHexString(),
        gradient.green.toHexString(),
        gradient.blue.toHexString()
    ).forEach { hexStr ->
        result +=
            if (hexStr.length == 1) "${hexStr}0"
            else hexStr
    }

    return result.also { println(it) }
}
