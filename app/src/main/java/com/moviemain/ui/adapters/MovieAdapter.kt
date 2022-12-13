package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.MovieItemBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.view.main.MovieListFragmentDirections

class MovieAdapter(private val movieList: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(movie: Movie) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + movie.poster_path
            Glide.with(binding.root.context)
                .load(posterFormat)
                .error(R.drawable.gradient)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
                val action = MovieListFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                    movie.poster_path,
                    movie.backdrop_path,
                    movie.vote_average.toFloat(),
                    movie.vote_count,
                    movie.overview,
                    movie.title,
                    movie.original_language,
                    movie.release_date
                )
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setData(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size
}