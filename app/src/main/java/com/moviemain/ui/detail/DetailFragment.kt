package com.moviemain.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.moviemain.R
import com.moviemain.application.Constants.POSTER_PATH_URL
import com.moviemain.application.Constants.YOUTUBE_BASE_URL
import com.moviemain.core.*
import com.moviemain.core.utils.*
import com.moviemain.data.model.Movie
import com.moviemain.databinding.FragmentDetailBinding
import com.moviemain.ui.detail.adapter.CreditsAdapter
import com.moviemain.ui.detail.adapter.CrewAdapter
import com.moviemain.ui.detail.adapter.DetailAdapter
import com.moviemain.ui.detail.state.ButtonsState
import com.moviemain.ui.detail.state.CreditsState
import com.moviemain.ui.detail.state.SimilarState
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val viewModel by viewModels<DetailViewModel>()

    private val detailAdapter: DetailAdapter = DetailAdapter()
    private val creditsAdapter: CreditsAdapter = CreditsAdapter()
    private val crewAdapter: CrewAdapter = CrewAdapter()

    private lateinit var movie: Movie

    private var isMovieFavorited: Boolean? = null

    private var btnHomepage: Button? = null
    private var btnHomepageAnim: Animation? = null

    private var btnWatchTrailer: Button? = null
    private var btnWatchTrailerAnim: Animation? = null

    private var colorEnabled: Int? = null
    private var colorDisable: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        colorEnabled = R.color.white
        colorDisable = R.color.grey_dark
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        with(binding) {
            try {
                isLoadingScreen(false)

                loadImage(requireContext(), POSTER_PATH_URL + movie.poster_path, binding.imgMovie)
                loadImage(
                    requireContext(),
                    POSTER_PATH_URL + movie.backdrop_path,
                    binding.imgBackground
                )

                txtMovieTitle.text = movie.title
                txtRating.text =
                    movie.vote_average.toString() + " " + "(" + movie.vote_count.toString() + " " +
                            getString(R.string.reviews) + ")"
                txtReleased.text = getString(R.string.released) + " " + movie.release_date
                txtLanguage.text = getString(R.string.language) + " " + movie.original_language

                if (movie.overview.isNullOrEmpty()) {
                    showToast(getString(R.string.no_data_for_description))
                } else {
                    txtDescription.text = movie.overview
                }

                showBtnHomepageAndWatchTrailer(movie.id!!)
                loadOverview()

                txtTitleCredits.setOnClickListener {
                    showCreditsMovies(movie.id!!)
                    loadCredits()
                }

                txtTitleSimilar.setOnClickListener {
                    showSimilarMovies(movie.id!!)
                    loadSimilar()
                }

            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }

    private fun showBtnHomepageAndWatchTrailer(id: Int) {
        enableOptions()
        viewModel.fetchHomepageAndTrailerMovie(id)
        viewModel.buttonsState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is ButtonsState.Loading -> {
                        hideElements(containerError.root, txtDescription)
                        isLoadingBtnHomePage(false)
                        isLoadingBtnWatchTrailer(false)
                    }

                    is ButtonsState.SuccessHomepage -> {
                        showElements(txtDescription)
                        binding.txtTitleOverview.setTextColorCompat(colorEnabled!!)
                        binding.dividerLine1.setBackgroundColorCompat(colorEnabled!!)

                        if (it.homepage.homepage?.isEmpty()!!) {
                            isLoadingBtnHomePage(false)
                        } else {
                            isLoadingBtnHomePage(true)
                            goToHomepage(it.homepage.homepage.toString())
                        }

                        if (resources.isLandscapeOrientation()) {
                            cardView.show()
                        }
                    }

                    is ButtonsState.SuccessTrailer -> {
                        showElements(txtDescription)
                        binding.txtTitleOverview.setTextColorCompat(colorEnabled!!)
                        binding.dividerLine1.setBackgroundColorCompat(colorEnabled!!)

                        if (it.trailer.results.isEmpty()) {
                            isLoadingBtnWatchTrailer(false)
                        } else {
                            isLoadingBtnWatchTrailer(true)
                            goToWatchTrailer((it.trailer.results.last().key.toString()))
                        }

                        if (resources.isLandscapeOrientation()) {
                            cardView.show()
                        }
                    }

                    is ButtonsState.Failure -> {
                        hideElements(txtDescription)
                        showElements(containerError.root)
                        isLoadingBtnHomePage(false)
                        isLoadingBtnWatchTrailer(false)

                        if (resources.isLandscapeOrientation()) {
                            cardView.hide()
                        }

                        btnRetryButtons()
                        disableOptions()

                        if (it.error != null) {
                            val errorMessage = getString(it.error.errorMessage)
                            containerError.textView.text = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun btnRetryButtons() {
        binding.containerError.btnRetry.setRetryAction {
            showDataDetails()
        }
    }

    private fun goToHomepage(homepage: String) {
        btnHomepage = binding.btnHomePage
        btnHomepageAnim = AnimationUtils.loadAnimation(context, R.anim.btn_anim)

        btnHomepage!!.goToHomepage(homepage)
    }

    private fun goToWatchTrailer(key: String) {
        btnWatchTrailer = binding.btnWatchTrailer
        btnWatchTrailerAnim = AnimationUtils.loadAnimation(context, R.anim.btn_anim)

        val youtubeUrl = YOUTUBE_BASE_URL + key

        btnWatchTrailer!!.goToWatchTrailer(youtubeUrl)
    }

    private fun showCreditsMovies(id: Int) {
        with(binding) {
            enableOptions()
            viewModel.fetchCreditsMovie(id)
            viewModel.creditsState.observe(viewLifecycleOwner) {
                when (it) {
                    is CreditsState.Loading -> {
                        showElements(progressbarOption)
                        hideElements(containerError.root)
                    }

                    is CreditsState.Success -> {
                        hideElements(progressbarOption)
                        binding.txtTitleCredits.setTextColorCompat(colorEnabled!!)
                        binding.dividerLine2.setBackgroundColorCompat(colorEnabled!!)

                        if (it.credits.cast?.isEmpty()!!) {
                            showToast(getString(R.string.no_data_for_credits))
                            return@observe
                        } else {
                            creditsAdapter.setCreditsMovieList(it.credits.cast)
                            setupCreditsRecyclerView()
                        }
                        if (!it.credits.crew?.isEmpty()!!) {
                            crewAdapter.setCrewMovieList(it.credits.crew.filter { crew -> crew.job == "Director" })
                            setupCrewRecyclerView()
                        }
                    }

                    is CreditsState.Failure -> {
                        hideElements(progressbarOption, rvMoviesCredits, rvMoviesCrew)
                        showElements(containerError.root)

                        btnRetryCredits(id)
                        disableOptions()

                        if (it.error != null) {
                            val errorMessage = getString(it.error.errorMessage)
                            containerError.textView.text = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun btnRetryCredits(id: Int) {
        binding.containerError.btnRetry.setRetryAction {
            showCreditsMovies(id)
        }
    }

    private fun setupCreditsRecyclerView() {
        binding.rvMoviesCredits.apply {
            adapter = ScaleInAnimationAdapter(creditsAdapter)
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_credits),
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            show()
        }
    }

    private fun setupCrewRecyclerView() {
        binding.rvMoviesCrew.apply {
            adapter = ScaleInAnimationAdapter(crewAdapter)
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_crew),
                StaggeredGridLayoutManager.HORIZONTAL
            )
            setHasFixedSize(true)
            show()
        }
    }

    private fun showSimilarMovies(id: Int) {
        with(binding) {
            enableOptions()
            viewModel.fetchSimilarMovies(id)
            viewModel.similarState.observe(viewLifecycleOwner) {
                when (it) {
                    is SimilarState.Loading -> {
                        showElements(progressbarOption)
                        hideElements(containerError.root)
                    }

                    is SimilarState.Success -> {
                        hideElements(progressbarOption)
                        binding.txtTitleSimilar.setTextColorCompat(colorEnabled!!)
                        binding.dividerLine3.setBackgroundColorCompat(colorEnabled!!)

                        if (it.similar.results.isEmpty()) {
                            showToast(getString(R.string.no_data_for_similar_movies))
                            return@observe
                        }
                        detailAdapter.setDetailMovieList(it.similar.results)
                        setupSimilarRecyclerView()
                    }

                    is SimilarState.Failure -> {
                        hideElements(progressbarOption, rvMoviesSimilar)
                        showElements(containerError.root)

                        btnRetrySimilar(id)
                        disableOptions()

                        if (it.error != null) {
                            val errorMessage = getString(it.error.errorMessage)
                            containerError.textView.text = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun btnRetrySimilar(id: Int) {
        binding.containerError.btnRetry.setRetryAction {
            showSimilarMovies(id)
        }
    }

    private fun setupSimilarRecyclerView() {
        binding.rvMoviesSimilar.apply {
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
        with(binding) {
            txtTitleOverview.setOnClickListener {
                showDataDetails()
                hideElements(rvMoviesCredits, rvMoviesCrew, rvMoviesSimilar, containerError.root)

                if (resources.isLandscapeOrientation()) {
                    cardView.show()
                }

                txtTitleOverview.setTextColorCompat(colorEnabled!!)
                txtTitleCredits.setTextColorCompat(colorDisable!!)
                txtTitleSimilar.setTextColorCompat(colorDisable!!)
                dividerLine1.setBackgroundColorCompat(colorEnabled!!)
                dividerLine2.setBackgroundColorCompat(colorDisable!!)
                dividerLine3.setBackgroundColorCompat(colorDisable!!)
            }
        }
    }

    private fun loadCredits() {
        with(binding) {
            isLoadingBtnHomePage(false)
            isLoadingBtnWatchTrailer(false)

            hideElements(txtDescription, rvMoviesSimilar)

            if (resources.isLandscapeOrientation()) {
                cardView.hide()
            }

            txtTitleOverview.setTextColorCompat(colorDisable!!)
            txtTitleCredits.setTextColorCompat(colorEnabled!!)
            txtTitleSimilar.setTextColorCompat(colorDisable!!)
            dividerLine1.setBackgroundColorCompat(colorDisable!!)
            dividerLine2.setBackgroundColorCompat(colorEnabled!!)
            dividerLine3.setBackgroundColorCompat(colorDisable!!)
        }
    }

    private fun loadSimilar() {
        with(binding) {
            isLoadingBtnHomePage(false)
            isLoadingBtnWatchTrailer(false)

            hideElements(rvMoviesCredits, rvMoviesCrew, txtDescription)

            if (resources.isLandscapeOrientation()) {
                cardView.hide()
            }

            txtTitleOverview.setTextColorCompat(colorDisable!!)
            txtTitleCredits.setTextColorCompat(colorDisable!!)
            txtTitleSimilar.setTextColorCompat(colorEnabled!!)
            dividerLine1.setBackgroundColorCompat(colorDisable!!)
            dividerLine2.setBackgroundColorCompat(colorDisable!!)
            dividerLine3.setBackgroundColorCompat(colorEnabled!!)
        }
    }

    private fun disableOptions() {
        with(binding) {
            txtTitleOverview.disableClick()
            txtTitleCredits.disableClick()
            txtTitleSimilar.disableClick()

            txtTitleOverview.setTextColorCompat(colorDisable!!)
            txtTitleCredits.setTextColorCompat(colorDisable!!)
            txtTitleSimilar.setTextColorCompat(colorDisable!!)
            dividerLine1.setBackgroundColorCompat(colorDisable!!)
            dividerLine2.setBackgroundColorCompat(colorDisable!!)
            dividerLine3.setBackgroundColorCompat(colorDisable!!)
        }
    }

    private fun enableOptions() {
        with(binding) {
            txtTitleOverview.enableClick()
            txtTitleCredits.enableClick()
            txtTitleSimilar.enableClick()
        }
    }

    private fun isLoadingScreen(loading: Boolean) {
        with(binding) {
            progressbar.isVisible = loading
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
            try {
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
            } catch (e: Exception) {
                showToast(getString(R.string.share_error))
            }
        }
    }
}