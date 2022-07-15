package com.moviemain.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moviemain.R
import com.moviemain.databinding.MovieItemBinding
import com.moviemain.ui.view.main.MovieListFragmentDirections

//class CarouselAdapter(private val upcomingList: List<Upcoming>) :
//    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
//
//    class CarouselViewHolder(private val binding: MovieItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun setData(upcoming: Upcoming) {
//            val imageUrl = "https://image.tmdb.org/t/p/w500"
//            val posterFormat = imageUrl + upcoming.poster_path
//            Glide.with(binding.root.context)
//                .load(posterFormat)
//                .error(R.drawable.gradient)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(binding.imgMovie)
//
//            binding.cvImgMovie.setOnClickListener {
////                it -> it.findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment)
//                val action = MovieListFragmentDirections.actionMovieFragmentToMovieDetailFragment(
//                    upcoming.poster_path.toString(),
//                    upcoming.backdrop_path.toString(),
//                    upcoming.vote_average.toFloat(),
//                    upcoming.vote_count.toInt(),
//                    upcoming.overview.toString(),
//                    upcoming.title.toString(),
//                    upcoming.original_language.toString(),
//                    upcoming.release_date.toString()
//                )
//                this.itemView.findNavController().navigate(action)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
//        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CarouselViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
//        holder.setData(upcomingList[position])
//    }
//
//    override fun getItemCount(): Int = upcomingList.size
//}