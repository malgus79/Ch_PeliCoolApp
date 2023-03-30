package com.moviemain.ui.bookmark

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.moviemain.R
import com.moviemain.core.Resource
import com.moviemain.core.utils.hide
import com.moviemain.core.utils.show
import com.moviemain.core.utils.showToast
import com.moviemain.databinding.FragmentBookmarkBinding
import com.moviemain.data.model.Movie
import com.moviemain.ui.bookmark.adapter.BookmarkAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator

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
                            rvMoviesBookmark.adapter?.notifyDataSetChanged()
                            rvMoviesBookmark.hide()
                            emptyContainer.root.show()
                            return@Observer
                        }
                        bookmarkAdapter.setMovieList(it.data)
                        setupBookmarkRecyclerView()
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast(getString(R.string.error_dialog_detail) + it.exception)
                        Log.d(ContentValues.TAG, "Error: " + it.exception)
                    }
                }
            }
        })
    }

    private fun setupBookmarkRecyclerView() {
        binding.rvMoviesBookmark.apply {
            adapter = ScaleInAnimationAdapter(bookmarkAdapter)
            itemAnimator = LandingAnimator().apply { addDuration = 1000 }
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_bookmark),
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
        }
    }

    override fun onMovieClick(movie: Movie, position: Int) {
        findNavController().navigate(
            BookmarkFragmentDirections.actionMenuBookmarkToMovieDetailFragment(movie)
        )
    }

    override fun onMovieLongClick(movie: Movie, position: Int) {
        viewModel.deleteFavoriteMovie(movie)
        showToast(getString(R.string.removed_movie))
    }
}