package com.moviemain.ui.view.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.moviemain.R
import com.moviemain.databinding.FragmentMovieDetailBinding
import com.moviemain.model.data.Movie
import com.moviemain.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private lateinit var binding: FragmentMovieDetailBinding
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var movie: Movie
    private var isMovieFavorited: Boolean? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMovieDetailBinding.bind(view)

        movie = Movie()

        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500/${args.posterImageUrl}")
            .centerCrop()
            .into(binding.imgMovie)

        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500/${args.backgroundImageUrl}")
            .centerCrop()
            .into(binding.imgBackground)

        binding.txtDescription.text = args.overview
        binding.txtMovieTitle.text = args.title
        binding.txtLanguage.text = "Language ${args.language}"
        binding.txtRating.text = "${args.voteAverage} (${args.voteCount} Reviews)"
        binding.txtReleased.text = "Released ${args.releaseDate}"

        fun updateButtonIcon() {
            isMovieFavorited = isMovieFavorited ?: return

            binding.fabBookmark.setImageResource(
                when {
                    isMovieFavorited!! -> R.drawable.ic_delete
                    else -> R.drawable.ic_bookmark_added
                }
            )
        }

        binding.fabBookmark.setOnClickListener {
            val isMovieFavorited = isMovieFavorited ?: return@setOnClickListener

            viewModel.saveOrDeleteFavoriteMovie(movie)
            this.isMovieFavorited = !isMovieFavorited
            updateButtonIcon()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            isMovieFavorited = viewModel.isMovieFavorite(movie)
            updateButtonIcon()
        }


    }
}