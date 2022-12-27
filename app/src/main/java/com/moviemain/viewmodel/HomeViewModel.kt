package com.moviemain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.moviemain.core.Resource
import com.moviemain.domain.RepositoryImpl
import com.moviemain.model.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {

    fun fetchMainMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(
                Resource.Success(
                    Triple(
                        repository.getNowPlayingMovies(),
                        repository.getTopRatedMovies(),
                        repository.getPopularMovies()
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun saveOrDeleteFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            if (repository.isMovieFavorite(movie)) {
                repository.deleteFavoriteMovie(movie)
//                Toast.makeText(this@MainViewModel, "", Toast.LENGTH_SHORT).show()
//                toastHelper.sendToast("Cocktail deleted from favorites")
            } else {
                repository.saveFavoriteMovie(movie)
//                toastHelper.sendToast("Cocktail saved to favorites")
            }
        }
    }

    suspend fun isMovieFavorite(movie: Movie): Boolean? =
        repository.isMovieFavorite(movie)
}

