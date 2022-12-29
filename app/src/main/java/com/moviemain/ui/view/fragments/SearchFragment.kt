package com.moviemain.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviemain.core.*
import com.moviemain.databinding.FragmentSearchBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.adapters.BookmarkAdapter
import com.moviemain.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), BookmarkAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: BookmarkAdapter  //uses the same adapter as BookmarFragment
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = BookmarkAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        setupRecyclerView()
        setupSearView()
        setupSearchMovies()

        return binding.root
    }

    private fun setupSearchMovies() {
        viewModel.fetchMovieList.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Resource.Loading -> {
                    binding.emptyContainer.root.hide()
                    binding.progressBar.show()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    if (it.data.isEmpty()) {
                        binding.rvMoviesSearch.hide()
                        binding.emptyContainer.root.show()
                        return@Observer
                    }
                    binding.rvMoviesSearch.show()
                    searchAdapter.setMovieList(it.data)
                    binding.emptyContainer.root.hide()
                }
                is Resource.Failure -> {
                    binding.progressBar.hide()
                    //showToast("Ocurrió un error al traer los datos ${it.exception}")
                }
            }
        })
    }

    private fun setupSearView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setMovieSearched(query!!)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvMoviesSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoviesSearch.adapter = searchAdapter
    }

    override fun onMovieClick(movie: Movie, position: Int) {
        findNavController().navigate(SearchFragmentDirections.actionMenuSearchToMovieDetailFragment(movie))
    }

    override fun onMovieLongClick(movie: Movie, position: Int) {
        //TODO
    }
}