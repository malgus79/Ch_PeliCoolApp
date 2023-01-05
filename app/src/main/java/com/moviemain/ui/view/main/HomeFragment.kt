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
import com.moviemain.viewmodel.main.HomeViewModel
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
                        containerLoading.root.show()
                    }
                    is Resource.Success -> {
                        containerLoading.root.hide()
                        concatAdapter.apply {
                            addAdapter(0, PopularConcatAdapter(HomeAdapter(it.data.third.results)))
                            addAdapter(1, TopRatedConcatAdapter(HomeAdapter(it.data.second.results)))
                            addAdapter(2, NowPlayingConcatAdapter(HomeAdapter(it.data.first.results)))
                        }
                        rvMovies.apply {
                            adapter = concatAdapter
                            show()
                        }
                        setupCarousel()
                    }
                    is Resource.Failure -> {
                        containerLoading.root.hide()
                        showToast("Ocurrió un error al obtener los datos ${it.exception}")
                    }
                }
            }
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
        binding.carousel.show()
        binding.imgCarousel.show()
    }
}