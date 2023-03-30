package com.moviemain.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.moviemain.core.Resource
import com.moviemain.domain.RepositoryMovie
import com.moviemain.model.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RepositoryMovie) : ViewModel() {

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

    fun fetchHomepageAndTrailerMovie(id: Int) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emit(
                    Resource.Success(
                        Pair(
                            repository.getHomepage(id),
                            repository.getTrailerMovie(id)
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun fetchSimilarMovies(id: Int) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emit(
                    Resource.Success(repository.getSimilarMovie(id))
                )
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun fetchCreditsMovie(id: Int) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emit(
                    Resource.Success(repository.getCreditsMovie(id))
                )
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
}