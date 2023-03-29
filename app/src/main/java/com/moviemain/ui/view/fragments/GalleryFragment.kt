package com.moviemain.ui.view.fragments

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator
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

        swipeRefresh()
        setupGalleryMovies()

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
                    setupGalleryMovies()
                }, 500)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupGalleryMovies() {
        viewModel.fetchUpcomingMovies().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is ResourcePaging.LoadingPaging -> {
                        if (swipeRefreshLayout.isRefreshing) {
                            containerLoading.root.hide()
                        } else {
                            containerLoading.root.show()
                        }
                    }
                    is ResourcePaging.SuccessPaging<*> -> {
                        swipeRefreshLayout.isRefreshing = false
                        if (it.data.results.isEmpty()) {
                            containerLoading.root.show()
                            return@observe
                        }
                        containerLoading.root.hide()
                        setupGalleryRecyclerView()
                        loadData()
                    }
                    is ResourcePaging.FailurePaging -> {
                        swipeRefreshLayout.isRefreshing = false
                        containerLoading.root.show()
                        showToast(getString(R.string.error_dialog_detail) + it.exception)
                        Log.d(ContentValues.TAG, "Error: " + it.exception)
                    }
                }
            }
        }
    }

    private fun setupGalleryRecyclerView() {
        with(binding) {
            rvMoviesUpComing.apply {
                adapter = ScaleInAnimationAdapter(galleryAdapter)
                itemAnimator = FlipInLeftYAnimator().apply { addDuration = 500 }
                layoutManager = StaggeredGridLayoutManager(
                    resources.getInteger(R.integer.main_columns),
                    StaggeredGridLayoutManager.VERTICAL
                )
                setHasFixedSize(true)
                show()
            }
            titleUpcoming.show()
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
}