package com.moviemain.ui.adapters.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.ItemMovieCreditsBinding
import com.moviemain.model.data.Cast

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
            Glide.with(binding.root.context)
                .load(posterFormat)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgCharacter)

            binding.txtName.text = creditsMovie.original_name
            binding.txtCharacter.text = creditsMovie.character
        }
    }
}
