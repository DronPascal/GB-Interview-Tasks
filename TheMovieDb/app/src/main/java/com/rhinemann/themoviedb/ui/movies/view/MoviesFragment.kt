package com.rhinemann.themoviedb.ui.movies.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rhinemann.themoviedb.R
import com.rhinemann.themoviedb.core.afterTextChanged
import com.rhinemann.themoviedb.databinding.FragmentMoviesBinding
import com.rhinemann.themoviedb.domain.models.Movie
import com.rhinemann.themoviedb.ui.movies.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MovieAdapter.OnItemClickListener {

    private val binding by viewBinding(FragmentMoviesBinding::bind)
    private val viewModel by viewModels<MoviesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_MoviesFragment_to_MovieDetailsFragment)
        }

        initRecyclerView()
        initSearchView()
    }

    private fun initSearchView() {
        with(binding.etSearchInput) {
            clearFocus()
            binding.root.requestFocus()

            val onNewQuery = { query: String ->
                binding.rvMovies.scrollToPosition(0)
                viewModel.searchMovies(query)
                this.clearFocus()
            }

            afterTextChanged(onNewQuery)
        }
    }

    private fun initRecyclerView() {
        val adapter = MovieAdapter(this)

        binding.apply {
            rvMovies.setHasFixedSize(true)
            rvMovies.itemAnimator = null
            rvMovies.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { adapter.retry() },
                footer = MovieLoadStateAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener { adapter.retry() }
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                pbSearch.isVisible = loadState.source.refresh is LoadState.Loading
                ivSearchIcon.isVisible = loadState.source.refresh is LoadState.NotLoading
                rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error

                // Empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvMovies.isVisible = false
                    tvTypeSmth.isVisible = true
                } else {
                    tvTypeSmth.isVisible = false
                }
            }
        }
    }

    override fun onItemClick(movie: Movie) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movie)
        findNavController().navigate(action)
    }

}