package com.moviemain.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.moviemain.core.Resource
import com.moviemain.domain.RepositoryMovie
import com.moviemain.model.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repository: RepositoryMovie) : ViewModel() {

    fun fetchFavoriteMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emitSource(repository.getFavoritesMovies().map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun deleteFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteFavoriteMovie(movie)
        }
    }
}