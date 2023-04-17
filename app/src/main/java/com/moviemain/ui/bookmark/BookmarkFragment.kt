package com.moviemain.ui.bookmark

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.moviemain.R
import com.moviemain.core.utils.hide
import com.moviemain.core.utils.hideElements
import com.moviemain.core.utils.setupRecyclerView
import com.moviemain.core.utils.showElements
import com.moviemain.core.utils.showToast
import com.moviemain.data.model.Movie
import com.moviemain.databinding.FragmentBookmarkBinding
import com.moviemain.domain.common.Resource
import com.moviemain.ui.bookmark.adapter.BookmarkAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import timber.log.Timber

@AndroidEntryPoint
class BookmarkFragment : Fragment(),
    BookmarkAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private val viewModel: BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        bookmarkAdapter = BookmarkAdapter(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBookmarkMovies()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupBookmarkMovies() {
        viewModel.fetchFavoriteMovies().observe(viewLifecycleOwner, Observer {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.data.isEmpty()) {
                            rvMoviesBookmark.adapter?.notifyDataSetChanged()
                            hideElements(rvMoviesBookmark)
                            showElements(emptyContainer.root)
                            return@Observer
                        }
                        bookmarkAdapter.setMovieList(it.data)
                        setupBookmarkRecyclerView()
                    }

                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast(getString(R.string.error_dialog_detail) + it.exception)
                        Timber.tag(ContentValues.TAG).d(it.exception)
                    }
                }
            }
        })
    }

    private fun setupBookmarkRecyclerView() {
        binding.rvMoviesBookmark.setupRecyclerView(
            bookmarkAdapter,
            resources.getInteger(R.integer.columns_bookmark),
            LandingAnimator(),
            true
        )
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