package com.moviemain.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.moviemain.application.di.IoDispatcher
import com.moviemain.domain.RepositoryMovie
import com.moviemain.domain.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositoryMovie,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    fun fetchMainMovies() = liveData(dispatcher) {
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
}

