package com.moviemain.ui.search

import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviemain.R
import com.moviemain.core.utils.hide
import com.moviemain.core.utils.hideElements
import com.moviemain.core.utils.setupRecyclerView
import com.moviemain.core.utils.show
import com.moviemain.core.utils.showElements
import com.moviemain.data.model.Movie
import com.moviemain.databinding.FragmentSearchBinding
import com.moviemain.domain.common.Resource
import com.moviemain.ui.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        searchAdapter = SearchAdapter(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMostWanted()
        setupSearchView()
        setupSearchMovies()
    }

    private fun setupMostWanted() {
        viewModel.fetchMostWanted().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {

                        if (it.data.results.isEmpty()) {
                            hideElements(rvTopRatedInSearch)
                            return@observe
                        }

                        if (!rvMoviesSearch.isVisible) {
                            hideElements(rvMoviesSearch)
                            showElements(linearTopRated)
                            setupMostWantedRecyclerView()
                            searchAdapter.setMovieList(it.data.results)
                        }
                    }

                    is Resource.Failure -> {
                        Timber.tag(TAG).d(it.exception)
                    }
                }
            }
        }
    }

    private fun setupMostWantedRecyclerView() {
        binding.rvTopRatedInSearch.setupRecyclerView(
            searchAdapter,
            resources.getInteger(R.integer.columns_bookmark),
            LandingAnimator(),
            true
        )
    }

    private fun setupSearchMovies() {
        viewModel.fetchMovieList.observe(viewLifecycleOwner, Observer {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        hideElements(emptyContainer.root)
                        showElements(progressBar)
                    }

                    is Resource.Success -> {
                        hideElements(progressBar, linearTopRated)

                        if (it.data.isEmpty()) {
                            hideElements(rvMoviesSearch)
                            showElements(emptyContainer.root)
                            return@Observer
                        }
                        setupSearchRecyclerView()
                        searchAdapter.setMovieList(it.data)
                    }

                    is Resource.Failure -> {
                        hideElements(progressBar)
                        Timber.tag(TAG).d(it.exception)
                    }
                }
            }
        })
    }

    private fun setupSearchRecyclerView() {
        binding.rvMoviesSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            show()
        }
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        viewModel.setMovieSearched(query)
                        linearTopRated.hide()
                    } else {
                        rvMoviesSearch.hide()
                        setupMostWanted()
                    }
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query!!.isNotEmpty()) {
                        viewModel.setMovieSearched(query)
                        linearTopRated.hide()
                    } else {
                        rvMoviesSearch.hide()
                    }
                    return true
                }
            })
        }
    }

    override fun onMovieClick(movie: Movie, position: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionMenuSearchToMovieDetailFragment(
                movie
            )
        )
    }
}