package com.moviemain.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.moviemain.application.di.IoDispatcher
import com.moviemain.data.model.Movie
import com.moviemain.domain.RepositoryMovie
import com.moviemain.domain.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: RepositoryMovie,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    fun fetchFavoriteMovies() = liveData(dispatcher) {
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