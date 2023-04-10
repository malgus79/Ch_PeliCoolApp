package com.moviemain.ui.search

import androidx.lifecycle.*
import com.moviemain.application.di.IoDispatcher
import com.moviemain.domain.RepositoryMovie
import com.moviemain.domain.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RepositoryMovie,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mutableMovieSearched = MutableLiveData<String>()

    fun setMovieSearched(queryMovie: String) {
        mutableMovieSearched.value = queryMovie
    }

    val fetchMovieList = mutableMovieSearched.distinctUntilChanged().switchMap {
        liveData(dispatcher) {
            emit(Resource.Loading)
            try {
                repository.getMovieByName(it).collectLatest {
                    emit(it)
                }
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }

    fun fetchMostWanted() = liveData(dispatcher) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(repository.getTopRatedMovies()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


}