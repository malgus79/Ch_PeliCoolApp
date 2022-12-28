package com.moviemain.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviemain.R
import com.moviemain.core.ResourcePaging
import com.moviemain.core.hide
import com.moviemain.databinding.FragmentGalleryBinding
import com.moviemain.model.data.MovieList
import com.moviemain.ui.adapters.GalleryAdapter
import com.moviemain.viewmodel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

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

        setupGalleryData()
        setupRecyclerView()
        loadData()

        return binding.root
    }

    private fun setupGalleryData() {
        viewModel.getUpcomingMovies()
        viewModel.movieUpcomingList.observe(viewLifecycleOwner) {
            when (it) {
                is ResourcePaging.SuccessPaging<*> -> {
                    setMovie(it.data)
                    showSpinnerLoading(false)
                }
                is ResourcePaging.FailurePaging -> {
                    showErrorDialog()
                    binding.rvMoviesUpComing.hide()
                }
                is ResourcePaging.LoadingPaging -> showSpinnerLoading(true)
                else -> {}
            }
        }
    }

    private fun setupRecyclerView() {
        galleryAdapter = GalleryAdapter()

        binding.rvMoviesUpComing.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.main_columns),
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.listData.collect {
                Log.d("aaa", "load: $it")
                galleryAdapter.submitData(it)
            }
        }
    }

    private fun setMovie(movieList: Response<MovieList>) {
        if (movieList.body()?.results.isNullOrEmpty()) {
            showSpinnerLoading(false)
            binding.rvMoviesUpComing.adapter = GalleryAdapter()
        }
    }

    private fun showSpinnerLoading(loading: Boolean) {
        binding.rvMoviesUpComing.isVisible = !loading
        binding.progressBar.isVisible = loading
    }

    private fun showErrorDialog() {
        showSpinnerLoading(false)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.error_dialog))
            .setMessage(getString(R.string.error_dialog_detail))
            .setPositiveButton(getString(R.string.try_again)) { _, _ -> viewModel.getUpcomingMovies() }
            .setNegativeButton(getString(R.string.ok)) { _, _ -> showSpinnerLoading(true) }
            .setCancelable(false)
            .show()
    }
}