package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.data.NowPlaying
import com.moviemain.databinding.MovieItemBinding
import com.moviemain.ui.MovieListFragmentDirections

class NowPlayingAdapter(private val nowPlayingList: List<NowPlaying>) :
    RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder>() {

    class NowPlayingViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(nowPlaying: NowPlaying) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + nowPlaying.poster_path
            Glide.with(binding.root.context)
                .load(posterFormat)
                .error(R.drawable.gradient)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
//                it -> it.findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment)
                val action = MovieListFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                    nowPlaying.poster_path.toString(),
                    nowPlaying.backdrop_path.toString(),
                    nowPlaying.vote_average.toFloat(),
                    nowPlaying.vote_count.toInt(),
                    nowPlaying.overview.toString(),
                    nowPlaying.title.toString(),
                    nowPlaying.original_language.toString(),
                    nowPlaying.release_date.toString()
                )
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NowPlayingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        holder.setData(nowPlayingList[position])
    }

    override fun getItemCount(): Int = nowPlayingList.size
}