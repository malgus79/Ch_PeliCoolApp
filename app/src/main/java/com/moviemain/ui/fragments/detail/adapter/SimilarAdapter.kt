package com.moviemain.ui.fragments.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.utils.loadImage
import com.moviemain.databinding.ItemMovieRowBinding
import com.moviemain.data.model.Movie
import com.moviemain.ui.fragments.detail.SimilarDetailFragmentDirections

class SimilarAdapter : RecyclerView.Adapter<SimilarAdapter.VieHolder>() {

    private var similarMovieList = listOf<Movie>()

    fun setSimilarMovieList(similarMovieList: List<Movie>) {
        this.similarMovieList = similarMovieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemMovieRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(similarMovieList[position])
    }

    override fun getItemCount(): Int = similarMovieList.size - 8

    inner class VieHolder(private val binding: ItemMovieRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(similarMovie: Movie) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + similarMovie.poster_path

            loadImage(binding.root.context, posterFormat, binding.imgMovie)

            binding.cvImgMovie.setOnClickListener {
                val action =
                    SimilarDetailFragmentDirections.actionSimilarDetailFragmentToMovieDetailFragment(
                        similarMovie
                    )
                this.itemView.findNavController().navigate(action)
            }
        }
    }
}