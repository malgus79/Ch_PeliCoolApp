package com.moviemain.viewmodel

import androidx.lifecycle.*
import com.moviemain.core.ResourceNotFoundException
import com.moviemain.core.State
import com.moviemain.data.PopularList
import com.moviemain.data.TopRatedList
import com.moviemain.data.UpcomingList
import com.moviemain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _popularList = MutableLiveData<State<PopularList>>()
    val popularList: LiveData<State<PopularList>> = _popularList

    //Downloads data from api
    fun getPopularMovies() {
        _popularList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val popularList = repository.getPopularMovies()
                if (popularList.data.isNullOrEmpty()) {
                    _popularList.postValue((State.Failure(ResourceNotFoundException())))
                } else {
                    _popularList.postValue(State.Success(popularList))
                }
            } catch (e: Exception) {
                _popularList.postValue(State.Failure(e))
            }
        }
    }

    private val _topRatedList = MutableLiveData<State<TopRatedList>>()
    val topRatedList: LiveData<State<TopRatedList>> = _topRatedList

    //Downloads data from api
    fun getTopRatedMovies() {
        _topRatedList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val topRatedList = repository.getTopRatedMovies()
                if (topRatedList.data.isNullOrEmpty()) {
                    _topRatedList.postValue((State.Failure(ResourceNotFoundException())))
                } else {
                    _topRatedList.postValue(State.Success(topRatedList))
                }
            } catch (e: Exception) {
                _topRatedList.postValue(State.Failure(e))
            }
        }
    }

    private val _upcomingList = MutableLiveData<State<UpcomingList>>()
    val upcomingList: LiveData<State<UpcomingList>> = _upcomingList

    //Downloads data from api
    fun getUpcomingMovies() {
        _upcomingList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val upcomingList = repository.getUpcomingMovies()
                if (upcomingList.data.isNullOrEmpty()) {
                    _upcomingList.postValue((State.Failure(ResourceNotFoundException())))
                } else {
                    _upcomingList.postValue(State.Success(upcomingList))
                }
            } catch (e: Exception) {
                _upcomingList.postValue(State.Failure(e))
            }
        }
    }
}

