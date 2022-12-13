package com.moviemain.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviemain.R
import com.moviemain.core.State
import com.moviemain.databinding.FragmentMovieListBinding
import com.moviemain.model.data.MovieList
import com.moviemain.ui.adapters.*
import com.moviemain.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel by viewModels<MovieListViewModel>()
    private val list = mutableListOf<CarouselItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        val carousel: ImageCarousel = binding.carousel
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/ehSQcx7fYCRe92FPRdOjVjlgM3W.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/zBk0guZ6NI2aHclb4sbrQdrrnOC.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/sXeWfpT1EhG7f4uBouqraOhmouH.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/q5v13A4zZ0ffXqDQMfxKcNu1xzQ.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/1D2R2wIgbTyXTPzmyJIKSbVN9wG.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/bPIm1SXYp5RQ3c4wP91JQRewIb8.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/uOnutpXJdDWyWzUCkApkahPSKuy.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/engWUYSxDogn8csr3wJOq4cOzna.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/1dtdayBxKoYsD3YjuRyi9lSRNcF.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/osYbtvqjMUhEXgkuFJOsRYVpq6N.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/2r5ZISrHQUQLBMdAJF3CDDAxp54.jpg"))
        carousel.addData(list)

        viewModel.getPopularMovies()
        viewModel.popularList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setPopularMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getPopularMovies() })
            }
        }

        viewModel.getTopRatedMovies()
        viewModel.topRatedList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setTopRatedMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getTopRatedMovies() })
            }
        }

        viewModel.getNowPlayingMovies()
        viewModel.nowPlayingList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setNowPlayingMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getNowPlayingMovies() })
            }
        }

        viewModel.getUpcomingMovies()
        viewModel.upcomingList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setUpComingMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getUpcomingMovies() })
            }
        }

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

    private fun setPopularMovies(popularList: MovieList) {
        showSpinnerLoading(false)
        binding.rvMoviesPopular.adapter = MovieAdapter(popularList.results)
    }

    private fun setTopRatedMovies(topRatedList: MovieList) {
        showSpinnerLoading(false)
        binding.rvMoviesTopRated.adapter = MovieAdapter(topRatedList.results)
    }

    private fun setNowPlayingMovies(nowPlayingList: MovieList) {
        showSpinnerLoading(false)
        binding.rvMoviesNowPlaying.adapter = MovieAdapter(nowPlayingList.results)
    }

    private fun setUpComingMovies(upcomingList: MovieList) {
        showSpinnerLoading(false)
        binding.rvMoviesUpComing.adapter = MovieAdapter(upcomingList.results)
    }

    private fun showSpinnerLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.rvMoviesPopular.isVisible = !loading
        binding.rvMoviesTopRated.isVisible = !loading
        binding.rvMoviesNowPlaying.isVisible = !loading
        binding.rvMoviesUpComing.isVisible = !loading
    }
}