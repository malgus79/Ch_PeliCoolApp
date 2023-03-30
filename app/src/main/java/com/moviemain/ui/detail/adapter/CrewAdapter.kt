package com.moviemain.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviemain.core.utils.loadImage
import com.moviemain.databinding.ItemMovieCrewBinding
import com.moviemain.data.model.Crew

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

            loadImage(binding.root.context, posterFormat, binding.imgCharacter)

            binding.txtName.text = crewMovie.original_name
            binding.txtCharacter.text = crewMovie.job
        }
    }
}
