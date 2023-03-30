package com.moviemain.ui.fragments.home

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.moviemain.R
import com.moviemain.core.Resource
import com.moviemain.core.utils.hide
import com.moviemain.core.utils.show
import com.moviemain.core.utils.showToast
import com.moviemain.databinding.FragmentHomeBinding
import com.moviemain.presentation.home.HomeViewModel
import com.moviemain.ui.fragments.home.adapter.HomeAdapter
import com.moviemain.ui.fragments.home.adapter.concat.NowPlayingConcatAdapter
import com.moviemain.ui.fragments.home.adapter.concat.PopularConcatAdapter
import com.moviemain.ui.fragments.home.adapter.concat.TopRatedConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val list = mutableListOf<CarouselItem>()
    private lateinit var concatAdapter: ConcatAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        concatAdapter = ConcatAdapter()

        swipeRefresh()
        setupMainMovies()

        return binding.root
    }

    private fun swipeRefresh() {
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.setColorSchemeResources(R.color.black)
                swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                Handler(Looper.getMainLooper()).postDelayed({
                    setupMainMovies()
                }, 500)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupMainMovies() {
        viewModel.fetchMainMovies().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        if (swipeRefreshLayout.isRefreshing) {
                            containerLoading.root.hide()
                        } else {
                            containerLoading.root.show()
                        }
                    }
                    is Resource.Success -> {
                        swipeRefreshLayout.isRefreshing = false
                        containerLoading.root.hide()

                        if (!rvMovies.isVisible) {
                            concatAdapter.apply {
                                addAdapter(
                                    0,
                                    PopularConcatAdapter(HomeAdapter(it.data.third.results))
                                )
                                addAdapter(
                                    1,
                                    TopRatedConcatAdapter(HomeAdapter(it.data.second.results))
                                )
                                addAdapter(
                                    2,
                                    NowPlayingConcatAdapter(HomeAdapter(it.data.first.results))
                                )
                            }
                        }
                        setupHomeMoviesRecyclerView()
                        setupCarousel()
                    }
                    is Resource.Failure -> {
                        swipeRefreshLayout.isRefreshing = false
                        containerLoading.root.hide()
                        showToast(getString(R.string.error_dialog_detail) + it.exception)
                        Log.d(ContentValues.TAG, "Error: " + it.exception)
                    }
                }
            }
        }
    }

    private fun setupHomeMoviesRecyclerView() {
        binding.rvMovies.apply {
            adapter = concatAdapter
            show()
        }
    }

    private fun setupCarousel() {
        val carousel: ImageCarousel = binding.carousel
        carousel.registerLifecycle(lifecycle)
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/rHyB15bJiZKZUR6ZNgvcUxy9pVq.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/jny1jvCkgpzc3C0axQsX9ADYcAl.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/gBBCBMXKzWRADtliUYfV69aVIcz.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/mYpT2R7639KvKZ668uoQGS2QNFp.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/oJJWjiMKExSi241NpKUqVIxWfH6.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/9ftcHkD55IEWgolXWhpQHwoFTsV.jpg"))
        //carousel.setData(list)
        carousel.addData(list)
        with(binding) {
            carousel.show()
            imgCarousel.show()
            splashScreenImage?.show()
            peliCoolApp?.show()
        }
    }
}