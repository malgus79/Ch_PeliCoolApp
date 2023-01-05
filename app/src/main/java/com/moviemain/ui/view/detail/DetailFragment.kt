package com.moviemain.ui.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.moviemain.R
import com.moviemain.core.common.Constants.POSTER_PATH_URL
import com.moviemain.core.showToast
import com.moviemain.databinding.FragmentDetailBinding
import com.moviemain.model.data.Movie
import com.moviemain.viewmodel.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var movie: Movie
    private var isMovieFavorited: Boolean? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        requireArguments().let {
            DetailFragmentArgs.fromBundle(it).also { args ->
                movie = args.movie
            }
        }

        showDataDetails()
        isMovieFavorited()
        updateButtonIcon()
        onClickShareMovie()

    }

    @SuppressLint("SetTextI18n")
    private fun showDataDetails() {
        Glide.with(requireContext())
            .load(POSTER_PATH_URL + movie.poster_path)
            .centerCrop()
            .into(binding.imgMovie)

        Glide.with(requireContext())
            .load(POSTER_PATH_URL + movie.backdrop_path)
            .centerCrop()
            .into(binding.imgBackground)

        binding.txtDescription.text = movie.overview
        binding.txtMovieTitle.text = movie.title
        binding.txtLanguage.text = "Language ${movie.original_language}"
        binding.txtRating.text = "${movie.vote_average} (${movie.vote_count} Reviews)"
        binding.txtReleased.text = "Released ${movie.release_date}"

    }

    private fun isMovieFavorited() {
        binding.fabBookmark.setOnClickListener {
            val isMovieFavorited = isMovieFavorited ?: return@setOnClickListener

         if (isMovieFavorited) {
             showToast(getString(R.string.removed_movie))
         } else  {
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
                MediaStore.Images.Media.insertImage(context?.contentResolver, bitmap, "", null)
            val bitmapUri = Uri.parse(bitmapPath.toString())
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            startActivity(Intent.createChooser(intent, "Peli Cool App"))
        }
    }
}

