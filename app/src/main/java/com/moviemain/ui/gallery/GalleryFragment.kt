package com.moviemain.ui.gallery

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
import com.moviemain.core.utils.*
import com.moviemain.databinding.FragmentGalleryBinding
import com.moviemain.ui.gallery.adapter.GalleryAdapter
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        galleryAdapter = GalleryAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh()
        setupGalleryMovies()
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
        binding.swipeRefreshLayout.hideRefresh()
    }

    private fun setupGalleryMovies() {
        viewModel.fetchUpcomingMovies()
        viewModel.galleryMovieState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    GalleryState.Loading -> {
                        hideElements(containerError.root)

                        containerLoading.root.apply {
                            if (swipeRefreshLayout.isRefreshing) hide() else show()
                        }
                    }
                    is GalleryState.Success -> {
                        swipeRefreshLayout.hideRefresh()
                        hideElements(containerError.root,containerLoading.root)

                        if (it.movies.isEmpty()) {
                            showElements(containerLoading.root)
                            return@observe
                        }

                        setupGalleryRecyclerView()
                        loadData()
                    }
                    is GalleryState.Failure -> {
                        swipeRefreshLayout.hideRefresh()
                        hideElements(containerLoading.root,rvMoviesUpComing)
                        showElements(containerError.root)

                        btnRetry()

                        if (it.error != null) {
                            val errorMessage = getString(it.error.errorMessage)
                            containerError.textView.text = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun btnRetry() {
        binding.containerError.btnRetry.setRetryAction {
            setupGalleryMovies()
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