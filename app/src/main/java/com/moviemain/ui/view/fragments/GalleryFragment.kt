package com.moviemain.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviemain.R
import com.moviemain.core.State
import com.moviemain.core.StatePaging
import com.moviemain.databinding.FragmentGalleryBinding
import com.moviemain.model.data.MovieList
import com.moviemain.ui.adapters.MovieAdapter
import com.moviemain.ui.adapters.PagingAdapter
import com.moviemain.viewmodel.GalleryFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var pagingAdapter: PagingAdapter
    private val viewModel: GalleryFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadData()

        viewModel.getUpcomingMovies()
        viewModel.movieUpcomingList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is StatePaging.SuccessPaging<*> -> {
                    setMovie(it.data)
                    showSpinnerLoading(false)
                }
                is StatePaging.FailurePaging -> {
                    showErrorDialog(callback = { viewModel.getUpcomingMovies() })
                    binding.rvMoviesUpComing.isVisible = false
                }
                is StatePaging.LoadingPaging -> showSpinnerLoading(true)
                else -> {}
            }
        })

        return binding.root
    }

    private fun setupRecyclerView(isLoad: Boolean = false) {
        pagingAdapter = PagingAdapter()

        binding.rvMoviesUpComing.apply {
            adapter = pagingAdapter
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
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun setMovie(movieList: Response<MovieList>) {
        if (movieList.body()?.results.isNullOrEmpty()) {
            showSpinnerLoading(false)
            binding.rvMoviesUpComing.adapter = PagingAdapter()
        }
    }

    private fun showSpinnerLoading(loading: Boolean) {
        binding.rvMoviesUpComing.isVisible = !loading
        binding.progressBar.isVisible = loading
    }

    private fun showErrorDialog(callback: (() -> Unit)? = null) {
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