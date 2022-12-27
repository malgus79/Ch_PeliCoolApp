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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviemain.R
import com.moviemain.core.Resource
import com.moviemain.databinding.FragmentBookmarkBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.adapters.FavoritesAdapter
import com.moviemain.ui.view.detail.MovieDetailFragmentArgs
import com.moviemain.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(), FavoritesAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val viewModel: BookmarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesAdapter = FavoritesAdapter(requireContext(),this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookmarkBinding.bind(view)

        setupRecyclerView()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getFavoriteMovies().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.root.visibility = View.VISIBLE
                        return@Observer
                    }
                    favoritesAdapter.setMovieList(result.data)
                }
                is Resource.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "An error ocurred ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvMoviesBookmark.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoviesBookmark.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        binding.rvMoviesBookmark.adapter = favoritesAdapter
    }

    override fun onMovieClick(movie: Movie, position: Int) {
        findNavController().navigate(BookmarkFragmentDirections.actionMenuBookmarkToMovieDetailFragment(
            movie.poster_path, movie.title, movie.vote_average.toFloat(), movie.overview.toInt(),
            movie.popularity.toString(), movie.original_title, movie.original_language, movie.release_date
        ))
    }

    override fun onMovieLongClick(movie: Movie, position: Int) {
        viewModel.deleteFavoriteMovie(movie)
        Toast.makeText(requireContext(), "Movie deleted !", Toast.LENGTH_SHORT).show()
    }
}