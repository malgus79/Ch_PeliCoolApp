package com.moviemain.ui.adapters.concat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.holder.BaseConcatHolder
import com.moviemain.databinding.RowPopularMoviesBinding
import com.moviemain.ui.adapters.HomeAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator

class PopularConcatAdapter(private val moviesAdapter: HomeAdapter) :
    RecyclerView.Adapter<BaseConcatHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseConcatHolder<*> {
        val itemBinding =
            RowPopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConcatViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseConcatHolder<*>, position: Int) {
        when (holder) {
            is ConcatViewHolder -> holder.bind(moviesAdapter)
            else -> throw IllegalArgumentException("No viewholder to show this data, did you forgot to add it to the onBindViewHolder?")
        }
    }

    override fun getItemCount(): Int = 1

    private inner class ConcatViewHolder(val binding: RowPopularMoviesBinding) :
        BaseConcatHolder<HomeAdapter>(binding.root) {
        override fun bind(adapter: HomeAdapter) {
            //binding.rvPopularMovies.adapter = adapter
            binding.rvPopularMovies.adapter = ScaleInAnimationAdapter(adapter)
            binding.rvPopularMovies.itemAnimator = LandingAnimator().apply { addDuration = 300 }
        }
    }
}