package com.moviemain.ui.adapters.concat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.BaseConcatHolder
import com.moviemain.databinding.NowPlayingMovieRowBinding
import com.moviemain.ui.adapters.MovieAdapter

class NowPlayingConcatAdapter(private val moviesAdapter: MovieAdapter) :
    RecyclerView.Adapter<BaseConcatHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseConcatHolder<*> {
        val itemBinding =
            NowPlayingMovieRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConcatViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseConcatHolder<*>, position: Int) {
        when (holder) {
            is ConcatViewHolder -> holder.bind(moviesAdapter)
            else -> throw IllegalArgumentException("No viewholder to show this data, did you forgot to add it to the onBindViewHolder?")
        }
    }

    override fun getItemCount(): Int = 1

    private inner class ConcatViewHolder(val binding: NowPlayingMovieRowBinding) :
        BaseConcatHolder<MovieAdapter>(binding.root) {
        override fun bind(adapter: MovieAdapter) {
            binding.rvNowPlayingMovies.adapter = adapter
        }
    }
}