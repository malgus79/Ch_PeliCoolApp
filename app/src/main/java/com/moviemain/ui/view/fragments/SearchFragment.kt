package com.moviemain.ui.view.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviemain.R
import com.moviemain.core.Resource
import com.moviemain.core.hide
import com.moviemain.core.show
import com.moviemain.core.showToast
import com.moviemain.databinding.FragmentSearchBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.adapters.SearchAdapter
import com.moviemain.viewmodel.fragments.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        searchAdapter = SearchAdapter(requireContext(), this)

        setupSearView()
        setupSearchMovies()

        return binding.root
    }

    private fun setupSearchMovies() {
        viewModel.fetchMovieList.observe(viewLifecycleOwner, Observer {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        emptyContainer.root.hide()
                        progressBar.show()
                    }
                    is Resource.Success -> {
                        progressBar.hide()
                        if (it.data.isEmpty()) {
                            rvMoviesSearch.hide()
                            emptyContainer.root.show()
                            return@Observer
                        }
                        //rvMoviesSearch.show()
                        setupSearchRecyclerView()
                        searchAdapter.setMovieList(it.data)
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        Log.d(TAG, "Error: " + it.exception)
                    }
                }
            }
        })
    }

    private fun setupSearView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.setMovieSearched(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query!!.isNotEmpty()) {
                    viewModel.setMovieSearched(query)
                } else {
                    binding.rvMoviesSearch.hide()
                }
                return true
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

    override fun onMovieClick(movie: Movie, position: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionMenuSearchToMovieDetailFragment(
                movie
            )
        )
    }

    override fun onMovieLongClick(movie: Movie, position: Int) {

    }
}