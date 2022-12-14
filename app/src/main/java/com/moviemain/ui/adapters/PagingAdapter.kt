package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.databinding.MovieItemBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.view.fragments.GalleryFragmentDirections
class PagingAdapter : PagingDataAdapter<Movie,
        PagingAdapter.ImageViewHolder>(diffCallback) {

    inner class ImageViewHolder(
        val binding: MovieItemBinding,
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
            MovieItemBinding.inflate(
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
                    .into(imgMovie)

                setOnClickListener {
                    val action = GalleryFragmentDirections.actionGalleryFragmentToMovieDetailFragment(
                        currMovie?.poster_path.toString(),
                        currMovie?.backdrop_path.toString(),
                        currMovie!!.vote_average.toFloat(),
                        currMovie.vote_count,
                        currMovie.overview,
                        currMovie.title,
                        currMovie.original_language,
                        currMovie.release_date
                    )
                    this.findNavController().navigate(action)
                }
            }
        }
    }
}