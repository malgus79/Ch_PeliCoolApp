package com.moviemain.ui.fragments.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.utils.loadImage
import com.moviemain.databinding.ItemMovieCreditsBinding
import com.moviemain.data.model.Cast

class CreditsAdapter : RecyclerView.Adapter<CreditsAdapter.VieHolder>() {

    private var creditsMovieList = listOf<Cast>()

    fun setCreditsMovieList(creditsMovieList: List<Cast>) {
        this.creditsMovieList = creditsMovieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemMovieCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(creditsMovieList[position])
    }

    override fun getItemCount(): Int {
        val creditsSizeTotal = creditsMovieList.size
        val creditsSizeCurrent = 12
        return if (creditsSizeCurrent > creditsSizeTotal) {
            creditsSizeTotal
        } else {
            creditsSizeCurrent
        }
    }

    inner class VieHolder(private val binding: ItemMovieCreditsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(creditsMovie: Cast) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + creditsMovie.profile_path

            loadImage(binding.root.context, posterFormat, binding.imgCharacter)

            binding.txtName.text = creditsMovie.original_name
            binding.txtCharacter.text = creditsMovie.character
        }
    }
}
