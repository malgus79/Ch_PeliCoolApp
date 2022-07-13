package com.moviemain.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviemain.R
import com.moviemain.core.State
import com.moviemain.data.PopularList
import com.moviemain.data.TopRatedList
import com.moviemain.data.UpcomingList
import com.moviemain.databinding.FragmentMovieListBinding
import com.moviemain.databinding.MovieItemBinding
import com.moviemain.ui.adapters.PopularAdapter
import com.moviemain.ui.adapters.TopRatedAdapter
import com.moviemain.ui.adapters.UpComingAdapter
import com.moviemain.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val viewModel by viewModels<MovieListViewModel>()
    private lateinit var binding: FragmentMovieListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        viewModel.getPopularMovies()
        viewModel.popularList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoadingPopular(true)
                is State.Success -> setPopularMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getPopularMovies() })
            }
        })

        viewModel.getTopRatedMovies()
        viewModel.topRatedList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoadingTopRated(true)
                is State.Success -> setTopRatedMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getTopRatedMovies() })
            }
        })

        viewModel.getUpComingMovies()
        viewModel.upComingList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoadingUpComing(true)
                is State.Success -> setUpComingMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getUpComingMovies() })
            }
        })

        return binding.root
    }

    private fun showErrorDialog(
        callback: (() -> Unit)? = null,
    ) {
        binding.progressBar.isVisible = false
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.error_dialog))
            .setMessage(getString(R.string.error_dialog_detail))
            .setPositiveButton(getString(R.string.try_again)) { _, _ -> callback?.invoke() }
            .show()
    }

    private fun setPopularMovies(popularList: PopularList) {
        showSpinnerLoadingPopular(false)
        binding.rvMoviesPopular.adapter = PopularAdapter(popularList.data)
    }

    private fun showSpinnerLoadingPopular(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.rvMoviesPopular.isVisible = !loading
    }

    private fun setTopRatedMovies(topRatedList: TopRatedList) {
        showSpinnerLoadingTopRated(false)
        binding.rvMoviesTopRated.adapter = TopRatedAdapter(topRatedList.data)
    }

    private fun showSpinnerLoadingTopRated(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.rvMoviesTopRated.isVisible = !loading
    }

    private fun setUpComingMovies(upcomingList: UpcomingList) {
        showSpinnerLoadingUpComing(false)
        binding.rvMoviesUpComing.adapter = UpComingAdapter(upcomingList.data)
    }

    private fun showSpinnerLoadingUpComing(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.rvMoviesUpComing.isVisible = !loading
    }
}