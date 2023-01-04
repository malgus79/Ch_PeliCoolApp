package com.moviemain.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviemain.domain.RepositoryImpl
import com.moviemain.model.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {

    fun saveOrDeleteFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            if (repository.isMovieFavorite(movie)) {
                repository.deleteFavoriteMovie(movie)
            } else {
                repository.saveFavoriteMovie(movie)
            }
        }
    }

    suspend fun isMovieFavorite(movie: Movie): Boolean =
        repository.isMovieFavorite(movie)

}