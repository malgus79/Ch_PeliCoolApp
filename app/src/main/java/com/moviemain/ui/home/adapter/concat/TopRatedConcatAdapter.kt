package com.moviemain.ui.home.adapter.concat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.holder.BaseConcatHolder
import com.moviemain.databinding.RowTopRatedMovieBinding
import com.moviemain.ui.home.adapter.HomeAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator

class TopRatedConcatAdapter(private val moviesAdapter: HomeAdapter) :
    RecyclerView.Adapter<BaseConcatHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseConcatHolder<*> {
        val itemBinding =
            RowTopRatedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConcatViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseConcatHolder<*>, position: Int) {
        when (holder) {
            is ConcatViewHolder -> holder.bind(moviesAdapter)
            else -> throw IllegalArgumentException("No viewholder to show this data, did you forgot to add it to the onBindViewHolder?")
        }
    }

    override fun getItemCount(): Int = 1

    private inner class ConcatViewHolder(val binding: RowTopRatedMovieBinding) :
        BaseConcatHolder<HomeAdapter>(binding.root) {
        override fun bind(adapter: HomeAdapter) {
            //binding.rvTopRatedMovies.adapter = adapter
            binding.rvTopRatedMovies.adapter = ScaleInAnimationAdapter(adapter)
            binding.rvTopRatedMovies.itemAnimator = LandingAnimator().apply { addDuration = 300 }
        }
    }
}