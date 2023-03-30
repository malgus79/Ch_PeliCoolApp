package com.moviemain.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.utils.loadImage
import com.moviemain.databinding.ItemMovieRowBinding
import com.moviemain.data.model.Movie
import com.moviemain.ui.detail.DetailFragmentDirections

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.VieHolder>() {

    private var detailMovieList = listOf<Movie>()

    fun setDetailMovieList(detailMovieList: List<Movie>) {
        this.detailMovieList = detailMovieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemMovieRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(detailMovieList[position])
    }

    override fun getItemCount(): Int = detailMovieList.size - 8

    inner class VieHolder(private val binding: ItemMovieRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(detailMovie: Movie) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + detailMovie.poster_path

            loadImage(binding.root.context, posterFormat, binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
                val action =
                    DetailFragmentDirections.actionMovieDetailFragmentToSimilarDetailFragment(
                        detailMovie
                    )
                this.itemView.findNavController().navigate(action)
            }
        }
    }
}