package com.moviemain.viewmodel

import androidx.lifecycle.*
import com.moviemain.core.ResourceNotFoundException
import com.moviemain.core.State
import com.moviemain.model.NowPlayingList
import com.moviemain.model.PopularList
import com.moviemain.model.TopRatedList
import com.moviemain.model.UpcomingList
import com.moviemain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    /* ---------------------------Popular movies request--------------------------- */
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

    /* ---------------------------Top Rated movies request--------------------------- */
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

    /* ---------------------------Now Playing movies request--------------------------- */
    private val _nowPlayingList = MutableLiveData<State<NowPlayingList>>()
    val nowPlayingList: LiveData<State<NowPlayingList>> = _nowPlayingList

    //Downloads data from api
    fun getNowPlayingMovies() {
        _nowPlayingList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val nowPlayingList = repository.getNow_PlayingMovies()
                if (nowPlayingList.data.isNullOrEmpty()) {
                    _nowPlayingList.postValue((State.Failure(ResourceNotFoundException())))
                } else {
                    _nowPlayingList.postValue(State.Success(nowPlayingList))
                }
            } catch (e: Exception) {
                _nowPlayingList.postValue(State.Failure(e))
            }
        }
    }

    /* ---------------------------Upcoming movies request--------------------------- */
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

