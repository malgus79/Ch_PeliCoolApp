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

class PopularAdapter(private val popularList: List<Movie>) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    class PopularViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(popular: Movie) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + popular.poster_path
            Glide.with(binding.root.context)
                .load(posterFormat)
                .error(R.drawable.gradient)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
//                it -> it.findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment)
                val action = MovieListFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                    popular.poster_path,
                    popular.backdrop_path,
                    popular.vote_average.toFloat(),
                    popular.vote_count,
                    popular.overview,
                    popular.title,
                    popular.original_language,
                    popular.release_date
                )
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.setData(popularList[position])
    }

    override fun getItemCount(): Int = popularList.size
}