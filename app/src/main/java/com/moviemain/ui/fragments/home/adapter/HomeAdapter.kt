package com.moviemain.ui.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.utils.loadImage
import com.moviemain.databinding.ItemMovieRowBinding
import com.moviemain.data.model.Movie
import com.moviemain.ui.fragments.home.HomeFragmentDirections

class HomeAdapter(private var movieList: List<Movie>) :
    RecyclerView.Adapter<HomeAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(movie: Movie) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + movie.poster_path

            loadImage(binding.root.context, posterFormat, binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
                val action = HomeFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                    movie
                )
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemMovieRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setData(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size
}