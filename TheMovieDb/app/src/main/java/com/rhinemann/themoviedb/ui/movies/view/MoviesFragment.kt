package com.rhinemann.themoviedb.ui.movies.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rhinemann.themoviedb.R
import com.rhinemann.themoviedb.core.afterTextChanged
import com.rhinemann.themoviedb.core.hideKeyboard
import com.rhinemann.themoviedb.core.onImeAction
import com.rhinemann.themoviedb.databinding.FragmentMoviesBinding
import com.rhinemann.themoviedb.domain.models.Movie
import com.rhinemann.themoviedb.ui.movies.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MovieAdapter.OnItemClickListener {

    private val binding by viewBinding(FragmentMoviesBinding::bind)
    private val viewModel by viewModels<MoviesViewModel>()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        MovieAdapter(this)
    }
    private val headerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MovieLoadStateAdapter { adapter.retry() }
    }
    private val footerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        headerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initSearchView()
    }

    private fun initSearchView() {
        with(binding.etSearchInput) {
            clearFocus()
            binding.ivSearchIcon.requestFocus()

            afterTextChanged { query: String ->
                if (query.isBlank()) return@afterTextChanged
                binding.rvMovies.scrollToPosition(0)
                viewModel.searchMovies(query)
                this.clearFocus()
            }
            onImeAction { hideKeyboard() }
        }
    }

    private fun initRecyclerView() {
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }
        val concatAdapter = ConcatAdapter(
            ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
            adapter, headerAdapter, footerAdapter
        )

        binding.apply {
            rvMovies.adapter = adapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            rvMovies.layoutManager = GridLayoutManager(requireContext(), spanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (concatAdapter.getItemViewType(position)) {
                            2 -> spanCount
                            1 -> 1
                            else -> -1
                        }
                    }
                }
            }
            rvMovies.setHasFixedSize(true)
            rvMovies.itemAnimator = null

            btnRetry.setOnClickListener { adapter.retry() }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                concatAdapter.addAdapter(headerAdapter)
                concatAdapter.addAdapter(footerAdapter)
            } else {
                concatAdapter.removeAdapter(headerAdapter)
                concatAdapter.removeAdapter(footerAdapter)
            }

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

        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onItemClick(movie: Movie) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movie)
        findNavController().navigate(action)
    }

}