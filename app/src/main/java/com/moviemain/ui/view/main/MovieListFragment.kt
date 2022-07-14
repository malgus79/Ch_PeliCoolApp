package com.moviemain.ui.view.main

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
import com.moviemain.model.NowPlayingList
import com.moviemain.model.PopularList
import com.moviemain.model.TopRatedList
import com.moviemain.model.UpcomingList
import com.moviemain.databinding.FragmentMovieListBinding
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
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        val carousel: ImageCarousel = binding.carousel
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/kAVRgw7GgK1CfYEJq8ME6EvRIgU.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/dHKfsdNcEPw7YIWFPIhqiuWrSAb.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/neMZH82Stu91d3iqvLdNQfqPPyl.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/rugyJdeoJm7cSJL1q4jBpTNbxyU.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/vpILbP9eOQEtdQgl4vgjZUNY07r.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/4Q1n3TwieoULnuaztu9aFjqHDTI.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/4zsihgkxMZ7MrflNCjkD3ySFJtc.jpg"))
        carousel.addData(list)

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

        viewModel.getNowPlayingMovies()
        viewModel.nowPlayingList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is State.Loading -> showSpinnerLoading(true)
                is State.Success -> setNowPlayingMovies(it.data)
                is State.Failure -> showErrorDialog(callback = { viewModel.getNowPlayingMovies() })
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

    private fun setNowPlayingMovies(nowPlayingList: NowPlayingList) {
        showSpinnerLoading(false)
        binding.rvMoviesNowPlaying.adapter = NowPlayingAdapter(nowPlayingList.data)
    }

    private fun setUpComingMovies(upcomingList: UpcomingList) {
        showSpinnerLoading(false)
        binding.rvMoviesUpComing.adapter = UpcomingAdapter(upcomingList.data)
    }

    private fun showSpinnerLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.rvMoviesPopular.isVisible = !loading
        binding.rvMoviesTopRated.isVisible = !loading
        binding.rvMoviesNowPlaying.isVisible = !loading
        binding.rvMoviesUpComing.isVisible = !loading
    }
}