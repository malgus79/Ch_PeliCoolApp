package com.moviemain.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.moviemain.R
import com.moviemain.core.ResourcePaging
import com.moviemain.core.hide
import com.moviemain.core.show
import com.moviemain.core.showToast
import com.moviemain.databinding.FragmentGalleryBinding
import com.moviemain.ui.adapters.GalleryAdapter
import com.moviemain.viewmodel.fragments.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var galleryAdapter: GalleryAdapter
    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        galleryAdapter = GalleryAdapter()

        setupGalleryMovies()

        return binding.root
    }

    private fun setupGalleryMovies() {
        viewModel.fetchUpcomingMovies().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is ResourcePaging.LoadingPaging -> {
                        containerLoading.root.show()
                    }
                    is ResourcePaging.SuccessPaging<*> -> {
                        if (it.data.body()?.results.isNullOrEmpty()) {
                            containerLoading.root.show()
                            return@observe
                        }
                        containerLoading.root.hide()
                        rvMoviesUpComing.adapter = galleryAdapter
                        setupGalleryRecyclerView()
                        loadData()
                    }
                    is ResourcePaging.FailurePaging -> {
                        containerLoading.root.show()
                        showToast("Ocurrió un error al obtener los datos ${it.exception}")
                    }
                }
            }
        }
    }

    private fun setupGalleryRecyclerView() {
        binding.rvMoviesUpComing.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(resources.getInteger(R.integer.main_columns), StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            show()
        }
        binding.titleUpcoming.show()
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.listData.collect {
                Log.d("aaa", "load: $it")
                galleryAdapter.submitData(it)
            }
        }
    }
}