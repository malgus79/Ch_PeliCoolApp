package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.MovieItemGalleryBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.view.fragments.GalleryFragmentDirections

class GalleryAdapter : PagingDataAdapter<Movie,
        GalleryAdapter.ImageViewHolder>(diffCallback) {

    inner class ImageViewHolder(
        val binding: MovieItemGalleryBinding,
    ) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            MovieItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currMovie = getItem(position)

        holder.binding.apply {

            holder.itemView.apply {

                val imageUrl = "https://image.tmdb.org/t/p/w500"
                val posterFormat = imageUrl + currMovie?.poster_path
                Glide.with(this)
                    .load(posterFormat)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.gradient)
                    .centerCrop()
                    .into(imgMovie)

                setOnClickListener {
                    val action = GalleryFragmentDirections.actionGalleryFragmentToMovieDetailFragment(
                        currMovie!!
                    )
                    this.findNavController().navigate(action)
                }
            }
        }
    }
}