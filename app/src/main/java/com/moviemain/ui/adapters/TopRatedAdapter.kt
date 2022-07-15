package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.MovieItemBinding
import com.moviemain.model.data.TopRated
import com.moviemain.ui.view.main.MovieListFragmentDirections

class TopRatedAdapter(private val topRatedList: List<TopRated>) :
    RecyclerView.Adapter<TopRatedAdapter.TopRatedViewHolder>() {

    class TopRatedViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(topRated: TopRated) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + topRated.poster_path
            Glide.with(binding.root.context)
                .load(posterFormat)
                .error(R.drawable.gradient)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
//                it -> it.findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment)
                val action = MovieListFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                    topRated.poster_path.toString(),
                    topRated.backdrop_path.toString(),
                    topRated.vote_average.toFloat(),
                    topRated.vote_count.toInt(),
                    topRated.overview.toString(),
                    topRated.title.toString(),
                    topRated.original_language.toString(),
                    topRated.release_date.toString()
                )
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopRatedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRatedViewHolder, position: Int) {
        holder.setData(topRatedList[position])
    }

    override fun getItemCount(): Int = topRatedList.size
}