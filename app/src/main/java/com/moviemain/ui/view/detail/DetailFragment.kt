package com.moviemain.ui.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.moviemain.R
import com.moviemain.core.Resource
import com.moviemain.core.common.Constants.POSTER_PATH_URL
import com.moviemain.core.common.Constants.YOUTUBE_BASE_URL
import com.moviemain.core.hide
import com.moviemain.core.show
import com.moviemain.core.showToast
import com.moviemain.databinding.FragmentDetailBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.adapters.DetailAdapter
import com.moviemain.viewmodel.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding

    //private val similarAdapter: SimilarAdapter = SimilarAdapter()
    private val detailAdapter: DetailAdapter = DetailAdapter()
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var movie: Movie

    private var isMovieFavorited: Boolean? = null

    private var btnHomepage: Button? = null
    private var btnHomepageAnim: Animation? = null

    private var btnWatchTrailer: Button? = null
    private var btnWatchTrailerAnim: Animation? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        requireArguments().let {
            DetailFragmentArgs.fromBundle(it).also { args ->
                movie = args.movie
            }
        }

        isLoadingScreen(true)

        showDataDetails()
        isMovieFavorited()
        updateButtonIcon()
        onClickShareMovie()

    }

    @SuppressLint("SetTextI18n")
    private fun showDataDetails() {
        try {
            isLoadingScreen(false)
            Glide.with(requireContext())
                .load(POSTER_PATH_URL + movie.poster_path)
                .centerCrop()
                .into(binding.imgMovie)

            Glide.with(requireContext())
                .load(POSTER_PATH_URL + movie.backdrop_path)
                .centerCrop()
                .into(binding.imgBackground)

            with(binding) {
                txtDescription.text = movie.overview
                txtMovieTitle.text = movie.title
                txtRating.text =
                    movie.vote_average.toString() + " " + "(" + movie.vote_count.toString() + " " +
                            getString(R.string.reviews) + ")"
                txtReleased.text = getString(R.string.released) + " " + movie.release_date
                txtLanguage.text = getString(R.string.language) + " " + movie.original_language
            }

            showBtnHomepageAndWatchTrailer(movie.id!!)
            showSimilarMovies(movie.id!!)

            loadOverview()
            loadSimilar()

        } catch (e: Exception) {
            showToast("${e.message}")
        }
    }

    private fun showBtnHomepageAndWatchTrailer(id: Int) {
        viewModel.fetchHomepageAndTrailerMovie(id).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data.first.homepage?.isEmpty()!!) {
                        isLoadingBtnHomePage(false)
                    } else {
                        isLoadingBtnHomePage(true)
                        goToHomepage(it.data.first.homepage.toString())
                    }
                    if (it.data.second.results.isEmpty()) {
                        isLoadingBtnWatchTrailer(false)
                    } else {
                        isLoadingBtnWatchTrailer(true)
                        goToWatchTrailer((it.data.second.results.last().key.toString()))
                    }
                }
                is Resource.Failure -> {
                    showToast(getString(R.string.error_dialog_detail))
                }
            }
        }
    }

    private fun goToHomepage(homepage: String) {
        btnHomepage = binding.btnHomePage
        btnHomepageAnim = AnimationUtils.loadAnimation(context, R.anim.btn_anim)

        binding.btnHomePage.setOnClickListener {
            btnHomepage!!.startAnimation(btnHomepageAnim)
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(homepage)))
            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }

    private fun goToWatchTrailer(key: String) {
        btnWatchTrailer = binding.btnWatchTrailer
        btnWatchTrailerAnim = AnimationUtils.loadAnimation(context, R.anim.btn_anim)

        val youtubeUrl = YOUTUBE_BASE_URL + key

        binding.btnWatchTrailer.setOnClickListener {
            btnWatchTrailer!!.startAnimation(btnWatchTrailerAnim)
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)))
            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }

    private fun showSimilarMovies(id: Int) {
        viewModel.fetchSimilarMovies(id).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data.results.isEmpty() && !binding.txtDescription.isVisible) {
                        showToast(getString(R.string.no_data))
                        return@observe
                    }
                    detailAdapter.setDetailMovieList(it.data.results)
                }
                is Resource.Failure -> {
                    showToast(getString(R.string.error_dialog_detail))
                }
            }
        }
    }

    private fun setupSimilarRecyclerView() {
        binding.rvMoviesSimilar.apply {
            //adapter = detailAdapter
            adapter = ScaleInAnimationAdapter(detailAdapter)
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_similar),
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            show()
        }
    }

    private fun loadOverview() {
        binding.txtTitleOverview.setOnClickListener {
            isLoadingOverviewOrSimilar(true)
            showDataDetails()
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.cardView.show()
            }
            with(binding) {
                txtTitleOverview.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                txtTitleSimilar.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.grey_dark)
                )
                dividerLine3.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.grey_dark)
                )
                dividerLine2.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }
        }
    }

    private fun loadSimilar() {
        binding.txtTitleSimilar.setOnClickListener {
            isLoadingOverviewOrSimilar(false)
            isLoadingBtnHomePage(false)
            isLoadingBtnWatchTrailer(false)
            setupSimilarRecyclerView()
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.cardView.hide()
            }
            with(binding) {
                txtTitleOverview.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.grey_dark)
                )
                txtTitleSimilar.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                dividerLine3.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                dividerLine2.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.grey_dark)
                )
            }
        }
    }

    private fun isLoadingScreen(loading: Boolean) {
        with(binding) {
            progressBar.isVisible = loading
            imgBackground.isVisible = !loading
            linearBookmark.isVisible = !loading
            linearShare.isVisible = !loading
            txtRating.isVisible = !loading
            txtReleased.isVisible = !loading
            txtLanguage.isVisible = !loading
            dividerLine.isVisible = !loading
            txtTitleOverview.isVisible = !loading
            txtDescription.isVisible = !loading
        }
    }

    private fun isLoadingBtnHomePage(loading: Boolean) {
        with(binding) {
            btnHomePage.isVisible = loading
            mcvHomePage.isVisible = loading
        }
    }

    private fun isLoadingBtnWatchTrailer(loading: Boolean) {
        with(binding) {
            btnWatchTrailer.isVisible = loading
            mcvWatchTrailer.isVisible = loading
        }
    }

    private fun isLoadingOverviewOrSimilar(loading: Boolean) {
        with(binding) {
            txtDescription.isVisible = loading
            rvMoviesSimilar.isVisible = !loading
        }
    }

    private fun isMovieFavorited() {
        binding.fabBookmark.setOnClickListener {
            val isMovieFavorited = isMovieFavorited ?: return@setOnClickListener

            if (isMovieFavorited) {
                showToast(getString(R.string.removed_movie))
            } else {
                showToast(getString(R.string.added_movie))
            }

            viewModel.saveOrDeleteFavoriteMovie(movie)
            this.isMovieFavorited = !isMovieFavorited
            updateButtonIcon()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            isMovieFavorited = viewModel.isMovieFavorite(movie)
            updateButtonIcon()
        }
    }

    private fun updateButtonIcon() {
        isMovieFavorited = isMovieFavorited ?: return

        binding.fabBookmark.setImageResource(
            when {
                isMovieFavorited!! -> R.drawable.ic_check
                else -> R.drawable.ic_add
            }
        )
    }

    private fun onClickShareMovie() {
        binding.fabShare.setOnClickListener {

            val bitmapDrawable = binding.imgMovie.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val bitmapPath =
                MediaStore.Images.Media.insertImage(
                    context?.contentResolver,
                    bitmap,
                    "IMAGE" + System.currentTimeMillis(),
                    null
                )
            val bitmapUri = Uri.parse(bitmapPath.toString())
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            startActivity(Intent.createChooser(intent, "Peli Cool App"))
        }
    }
}

