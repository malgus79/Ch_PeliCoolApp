package com.moviemain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviemain.core.ResourceNotFoundException
import com.moviemain.core.State
import com.moviemain.domain.Repository
import com.moviemain.model.data.MovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    /* ---------------------------Popular movies request--------------------------- */
    private val _popularList = MutableLiveData<State<MovieList>>()
    val popularList: LiveData<State<MovieList>> = _popularList

    //Downloads data from api
    fun getPopularMovies() {
        _popularList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val popularList = repository.getPopularMovies()
                if (popularList.results.isEmpty()) {
                    _popularList.postValue((State.Failure(ResourceNotFoundException())))
//                    repository.getAllMoviesFromDatabase()
                } else {
//                    repository.clearMovies()
                    _popularList.postValue(State.Success(popularList))
//                    repository.insertMovies(MovieEntity())
                }
            } catch (e: Exception) {
                _popularList.postValue(State.Failure(e))
            }
        }
    }

    /* ---------------------------Top Rated movies request--------------------------- */
    private val _topRatedList = MutableLiveData<State<MovieList>>()
    val topRatedList: LiveData<State<MovieList>> = _topRatedList

    //Downloads data from api
    fun getTopRatedMovies() {
        _topRatedList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val topRatedList = repository.getTopRatedMovies()
                if (topRatedList.results.isEmpty()) {
                    _topRatedList.postValue((State.Failure(ResourceNotFoundException())))
//                    repository.getAllMoviesFromDatabase(MovieEntity())
                } else {
//                    repository.clearMovies()
                    _topRatedList.postValue(State.Success(topRatedList))
//                    repository.saveMovieToLocalDataBase(MovieEntity())
                }
            } catch (e: Exception) {
                _topRatedList.postValue(State.Failure(e))
            }
        }
    }

    /* ---------------------------Now Playing movies request--------------------------- */
    private val _nowPlayingList = MutableLiveData<State<MovieList>>()
    val nowPlayingList: LiveData<State<MovieList>> = _nowPlayingList

    //Downloads data from api
    fun getNowPlayingMovies() {
        _nowPlayingList.postValue(State.Loading())
        viewModelScope.launch {
            try {
                val nowPlayingList = repository.getNowPlayingMovies()
                if (nowPlayingList.results.isEmpty()) {
                    _nowPlayingList.postValue((State.Failure(ResourceNotFoundException())))
//                    repository.getAllMoviesFromDatabase(MovieEntity())
                } else {
//                    repository.clearMovies()
                    _nowPlayingList.postValue(State.Success(nowPlayingList))
//                    repository.saveMovieToLocalDataBase(MovieEntity())
                }
            } catch (e: Exception) {
                _nowPlayingList.postValue(State.Failure(e))
            }
        }
    }
}

