package com.rhinemann.themoviedb.ui.movie_details.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rhinemann.themoviedb.R
import com.rhinemann.themoviedb.core.showSnackbar
import com.rhinemann.themoviedb.core.toVisible
import com.rhinemann.themoviedb.databinding.FragmentMovieDetailsBinding
import com.rhinemann.themoviedb.domain.models.Movie
import com.rhinemann.themoviedb.ui.movie_details.viewmodel.MovieDetailsState
import com.rhinemann.themoviedb.ui.movie_details.viewmodel.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val args by navArgs<MovieDetailsFragmentArgs>()
    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val viewModel: MovieDetailsViewModel by viewModels()
    private val castAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CastAdapter(onItemClick = viewModel::onItemClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onMovieIdReady(args.movie.id)
        initScreenWithBasicMovieModel(args.movie)
        setupReviewsList()

        viewModel.state.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: MovieDetailsState) =
        when (state) {
            is MovieDetailsState.Loading -> showLoading(show = true)
            is MovieDetailsState.Result -> renderResult(state)
            is MovieDetailsState.Error -> showSnackbar(R.string.error_getting_movie_info)
        }

    @SuppressLint("SetTextI18n")
    private fun renderResult(state: MovieDetailsState.Result) {
        with(state.movie) {
            binding.tvDetails.text = "%s\n%s\n%s".format(
                if (movie.date.isNotBlank()) getString(R.string.released) + ": " + movie.date else "",
                movie.runtime.ifBlank { "" },
                movie.genres.joinToString(separator = ", ")
            )
            binding.tvDetails.setLineSpacing(10F, 1F)
            castAdapter.submitList(cast)
        }
        showLoading(false)
    }

    private fun showLoading(show: Boolean) {
        binding.pbMovieDetailsInfo.isVisible = show
        binding.tvCastTitle.isVisible = !show
        binding.rvCast.isVisible = !show
    }

    private fun setupReviewsList() {
        binding.rvCast.adapter = castAdapter
    }

    private fun initScreenWithBasicMovieModel(movie: Movie) {
        with(binding) {
            Glide.with(requireContext())
                .load(movie.posterUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_cinema)
                .into(ivPoster)

            Glide.with(requireContext())
                .load(movie.backgroundUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_cinema)
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