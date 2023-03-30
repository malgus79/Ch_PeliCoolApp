package com.moviemain.ui.fragments.bookmark.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.holder.BaseViewHolder
import com.moviemain.core.utils.loadImage
import com.moviemain.databinding.ItemMovieSearchBinding
import com.moviemain.data.model.Movie

class BookmarkAdapter(
    private val context: Context,
    private val itemClickListener: OnMovieClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var movieList = listOf<Movie>()

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie, position: Int)
        fun onMovieLongClick(movie: Movie, position: Int)
    }

    fun setMovieList(movieList: List<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            ItemMovieSearchBinding.inflate(LayoutInflater.from(context), parent, false)
//        return MainViewHolder(itemBinding)

        val holder = MainViewHolder(itemBinding)

        holder.itemView.setOnClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener

            itemClickListener.onMovieClick(movieList[position], position)
        }

        holder.itemView.setOnLongClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnLongClickListener true

            itemClickListener.onMovieLongClick(movieList[position], position)

            return@setOnLongClickListener true
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

        override fun bind(item: Movie, position: Int): Unit = with(binding) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + item.poster_path

            loadImage(context, posterFormat, imgMovie)

            binding.txtTitulo.text = item.title
        }
    }
}