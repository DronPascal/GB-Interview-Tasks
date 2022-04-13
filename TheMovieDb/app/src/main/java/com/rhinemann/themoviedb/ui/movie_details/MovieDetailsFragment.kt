package com.rhinemann.themoviedb.ui.movie_details

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rhinemann.themoviedb.R
import com.rhinemann.themoviedb.core.toVisible
import com.rhinemann.themoviedb.databinding.FragmentMovieDetailsBinding
import com.rhinemann.themoviedb.domain.models.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val args by navArgs<MovieDetailsFragmentArgs>()

    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScreenWithBasicMovieModel(args.movie)
    }

    private fun initScreenWithBasicMovieModel(movie: Movie) {
        with(binding) {
            Glide.with(requireContext())
                .load(movie.posterUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivPoster)

            Glide.with(requireContext())
                .load(movie.backgroundUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivBackground)

            tvName.text = movie.name
            tvDetails.text = ""
            tvOverview.text = movie.overview
            if (movie.overview.isNotBlank()) tvOverviewTitle.toVisible()
            tvRating.text = movie.rating.toString()
            pbRating.progressTintList =
                ColorStateList.valueOf(Color.parseColor(movie.ratingColorHex))
            pbRating.progress = movie.rating
        }
    }

}