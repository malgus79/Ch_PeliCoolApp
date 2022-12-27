package com.moviemain.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviemain.core.BaseViewHolder
import com.moviemain.databinding.FavoritesRowBinding
import com.moviemain.model.data.Movie

class FavoritesAdapter(
    private val context: Context,
    private val itemClickListener: OnCocktailClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var cocktailList = listOf<Movie>()

    interface OnCocktailClickListener {
        fun onCocktailClick(cocktail: Movie, position: Int)
        fun onCocktailLongClick(cocktail: Movie, position: Int)
    }

    fun setCocktailList(cocktailList: List<Movie>) {
        this.cocktailList = cocktailList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = FavoritesRowBinding.inflate(LayoutInflater.from(context), parent, false)
//        return MainViewHolder(itemBinding)

        val holder = MainViewHolder(itemBinding)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener

            itemClickListener.onCocktailClick(cocktailList[position], position)
        }

        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnLongClickListener true

            itemClickListener.onCocktailLongClick(cocktailList[position], position)

            return@setOnLongClickListener true
        }

        return holder
    }

    override fun getItemCount(): Int = cocktailList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(cocktailList[position], position)
        }
    }

    private inner class MainViewHolder(private val binding: FavoritesRowBinding) :
        BaseViewHolder<Movie>(binding.root) {
        override fun bind(item: Movie, position: Int) = with(binding) {
            Glide.with(context).load(item.backdrop_path).centerCrop().into(imgCocktail)
            txtTitulo.text = item.title
        }
    }
}