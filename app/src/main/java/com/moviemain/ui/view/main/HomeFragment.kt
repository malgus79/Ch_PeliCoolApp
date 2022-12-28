package com.moviemain.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.moviemain.core.Resource
import com.moviemain.core.hide
import com.moviemain.core.show
import com.moviemain.core.showToast
import com.moviemain.databinding.FragmentHomeBinding
import com.moviemain.ui.adapters.HomeAdapter
import com.moviemain.ui.adapters.concat.NowPlayingConcatAdapter
import com.moviemain.ui.adapters.concat.PopularConcatAdapter
import com.moviemain.ui.adapters.concat.TopRatedConcatAdapter
import com.moviemain.viewmodel.HomeViewModel
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

        setupMainMovies()

        return binding.root
    }

    private fun setupMainMovies() {
        viewModel.fetchMainMovies().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        progressBar.show()
                    }
                    is Resource.Success -> {
                        progressBar.hide()
                        setupCarousel()
                        concatAdapter.apply {
                            addAdapter(0, PopularConcatAdapter(HomeAdapter(it.data.third.results)))
                            addAdapter(1, TopRatedConcatAdapter(HomeAdapter(it.data.second.results)))
                            addAdapter(2, NowPlayingConcatAdapter(HomeAdapter(it.data.first.results)))
                        }
                        rvMovies.adapter = concatAdapter
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast("Ocurrió un error al obtener los datos ${it.exception}")
                    }
                }
            }
        }
    }

    private fun setupCarousel() {
        val carousel: ImageCarousel = binding.carousel
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/ehSQcx7fYCRe92FPRdOjVjlgM3W.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/zBk0guZ6NI2aHclb4sbrQdrrnOC.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/sXeWfpT1EhG7f4uBouqraOhmouH.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/q5v13A4zZ0ffXqDQMfxKcNu1xzQ.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/1D2R2wIgbTyXTPzmyJIKSbVN9wG.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/bPIm1SXYp5RQ3c4wP91JQRewIb8.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/uOnutpXJdDWyWzUCkApkahPSKuy.jpg"))
        list.add(CarouselItem("https://image.tmdb.org/t/p/w500/engWUYSxDogn8csr3wJOq4cOzna.jpg"))
        carousel.addData(list)
        binding.carousel.show()
    }
}