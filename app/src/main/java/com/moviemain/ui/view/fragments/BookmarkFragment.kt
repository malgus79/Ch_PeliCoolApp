package com.moviemain.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.moviemain.databinding.FragmentBookmarkBinding
import com.moviemain.model.data.Movie
import com.moviemain.ui.view.detail.MovieDetailFragmentArgs
import com.moviemain.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel: BookmarkViewModel by viewModels()

    private lateinit var movie: Movie
    private var isMovieFavorited: Boolean? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}