package com.moviemain.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviemain.R
import com.moviemain.core.State
import com.moviemain.data.PopularList
import com.moviemain.data.TopRatedList
import com.moviemain.data.UpcomingList
import com.moviemain.databinding.FragmentMovieListBinding
import com.moviemain.ui.adapters.PopularAdapter
import com.moviemain.ui.adapters.TopRatedAdapter
import com.moviemain.ui.adapters.UpcomingAdapter
import com.moviemain.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel by viewModels<MovieListViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        viewModel.getPopularMovies()
        viewModel.popularList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setPopularMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getPopularMovies() })
            }
        })

        viewModel.getTopRatedMovies()
        viewModel.topRatedList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setTopRatedMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getTopRatedMovies() })
            }
        })

        viewModel.getUpcomingMovies()
        viewModel.upcomingList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setUpComingMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getUpcomingMovies() })
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
        showSpinnerLoading(false)
        binding.rvMoviesPopular.adapter = PopularAdapter(popularList.data)
    }

    private fun setTopRatedMovies(topRatedList: TopRatedList) {
        showSpinnerLoading(false)
        binding.rvMoviesTopRated.adapter = TopRatedAdapter(topRatedList.data)
    }

    private fun setUpComingMovies(upcomingList: UpcomingList) {
        showSpinnerLoading(false)
        binding.rvMoviesUpComing.adapter = UpcomingAdapter(upcomingList.data)
    }

    private fun showSpinnerLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.rvMoviesPopular.isVisible = !loading
        binding.rvMoviesTopRated.isVisible = !loading
        binding.rvMoviesUpComing.isVisible = !loading
    }
}