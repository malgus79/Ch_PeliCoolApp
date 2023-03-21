package com.moviemain.ui.adapters.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.ItemMovieCrewBinding
import com.moviemain.model.data.Crew

class CrewAdapter : RecyclerView.Adapter<CrewAdapter.VieHolder>() {

    private var crewMovieList = listOf<Crew>()

    fun setCrewMovieList(crewMovieList: List<Crew>) {
        this.crewMovieList = crewMovieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemMovieCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(crewMovieList[position])
    }

    override fun getItemCount(): Int = crewMovieList.size

    inner class VieHolder(private val binding: ItemMovieCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(crewMovie: Crew) {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            val posterFormat = imageUrl + crewMovie.profile_path
            Glide.with(binding.root.context)
                .load(posterFormat)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgCharacter)

            binding.txtName.text = crewMovie.original_name
            binding.txtCharacter.text = crewMovie.job
        }
    }
}
