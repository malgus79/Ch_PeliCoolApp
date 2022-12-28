package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.MovieItemRowBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.view.main.HomeFragmentDirections

class HomeAdapter(private var movieList: List<Movie>) :
    RecyclerView.Adapter<HomeAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: MovieItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(movie: Movie) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + movie.poster_path
            Glide.with(binding.root.context)
                .load(posterFormat)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
                val action = HomeFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                    movie
                )
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setData(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size
}