package com.moviemain.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviemain.R
import com.moviemain.core.Resource
import com.moviemain.databinding.FragmentHomeBinding
import com.moviemain.ui.adapters.MovieAdapter
import com.moviemain.ui.adapters.concat.NowPlayingConcatAdapter
import com.moviemain.ui.adapters.concat.PopularConcatAdapter
import com.moviemain.ui.adapters.concat.TopRatedConcatAdapter
import com.moviemain.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val list = mutableListOf<CarouselItem>()
    private lateinit var concatAdapter: ConcatAdapter
    private val viewModel by viewModels<MovieListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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

        setupMainMovies()

        return binding.root
    }

    private fun setupMainMovies() {
        concatAdapter = ConcatAdapter()
        viewModel.fetchMainMovies().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(0, PopularConcatAdapter(MovieAdapter(it.data.first.results)))
                        addAdapter(1, TopRatedConcatAdapter(MovieAdapter(it.data.second.results)))
                        addAdapter(2, NowPlayingConcatAdapter(MovieAdapter(it.data.third.results)))
                    }
                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog()
                }
            }
        })
    }

    private fun showErrorDialog() {
        binding.progressBar.isVisible = false
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.error_dialog))
            .setMessage(getString(R.string.error_dialog_detail))
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
//            .setPositiveButton(getString(R.string.try_again)) { _, _ -> callback?.invoke() }
            .show()
    }
}