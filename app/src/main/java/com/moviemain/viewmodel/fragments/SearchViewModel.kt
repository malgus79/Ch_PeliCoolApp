package com.moviemain.viewmodel.fragments

import androidx.lifecycle.*
import com.moviemain.core.Resource
import com.moviemain.domain.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {

    private val mutableMovieSearched = MutableLiveData<String>()

    fun setMovieSearched(queryMovie: String) {
        mutableMovieSearched.value = queryMovie
    }

    val fetchMovieList = mutableMovieSearched.distinctUntilChanged().switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
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
}