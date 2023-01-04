package com.moviemain.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.moviemain.databinding.FragmentBookmarkBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.adapters.BookmarkAdapter
import com.moviemain.viewmodel.fragments.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(), BookmarkAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private val viewModel: BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        bookmarkAdapter = BookmarkAdapter(requireContext(), this)

        setupRecyclerView()
        setupBookmarkMovies()

        return binding.root
    }

    private fun setupBookmarkMovies() {
        viewModel.fetchFavoriteMovies().observe(viewLifecycleOwner, Observer {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.data.isEmpty()) {
                            binding.rvMoviesBookmark.adapter?.notifyDataSetChanged()
                            rvMoviesBookmark.hide()
                            emptyContainer.root.show()
                            return@Observer
                        }
                        bookmarkAdapter.setMovieList(it.data)
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast("Ocurrió un error al obtener los datos ${it.exception}")
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvMoviesBookmark.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoviesBookmark.adapter = bookmarkAdapter
        binding.rvMoviesBookmark.setHasFixedSize(true)
    }

    override fun onMovieClick(movie: Movie, position: Int) {
        findNavController().navigate(
            BookmarkFragmentDirections.actionMenuBookmarkToMovieDetailFragment(movie))
    }

    override fun onMovieLongClick(movie: Movie, position: Int) {
        viewModel.deleteFavoriteMovie(movie)
        Toast.makeText(requireContext(), R.string.removed_movie, Toast.LENGTH_SHORT).show()
    }
}