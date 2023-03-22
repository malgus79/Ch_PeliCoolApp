package com.moviemain.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.core.holder.BaseViewHolder
import com.moviemain.databinding.ItemMovieSearchBinding
import com.moviemain.model.data.Movie

class SearchAdapter(
    private val context: Context,
    private val itemClickListener: OnMovieClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var movieList = listOf<Movie>()

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie, position: Int)
    }

    fun setMovieList(movieList: List<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = ItemMovieSearchBinding.inflate(LayoutInflater.from(context), parent, false)
//        return MainViewHolder(itemBinding)

        val holder = MainViewHolder(itemBinding)

        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener

            itemClickListener.onMovieClick(movieList[position], position)
        }

        return holder
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(movieList[position], position)
        }
    }

    private inner class MainViewHolder(private val binding: ItemMovieSearchBinding) :
        BaseViewHolder<Movie>(binding.root) {

        override fun bind(item: Movie, position: Int) = with(binding) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + item.poster_path
            Glide.with(context)
                .load(posterFormat)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(imgMovie)

            txtTitulo.text = item.title

        }
    }
}