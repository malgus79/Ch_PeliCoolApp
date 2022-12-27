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

        requireArguments().let {
            MovieDetailFragmentArgs.fromBundle(it).also { args ->
                movie = args.movie
            }
        }

        binding = FragmentMovieDetailBinding.bind(view)

//        movie = Movie()

        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
            .centerCrop()
            .into(binding.imgMovie)

        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500/${movie.backdrop_path}")
            .centerCrop()
            .into(binding.imgBackground)

        binding.txtDescription.text = movie.overview
        binding.txtMovieTitle.text = movie.title
        binding.txtLanguage.text = "Language ${movie.original_language}"
        binding.txtRating.text = "${movie.vote_average} (${movie.vote_count} Reviews)"
        binding.txtReleased.text = "Released ${movie.release_date}"

        fun updateButtonIcon() {
            isMovieFavorited = isMovieFavorited ?: return

            binding.fabBookmark.setImageResource(
                when {
                    isMovieFavorited!! -> R.drawable.ic_check
                    else -> R.drawable.ic_add
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