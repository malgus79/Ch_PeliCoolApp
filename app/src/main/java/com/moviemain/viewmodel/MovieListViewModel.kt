package com.moviemain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviemain.core.ResourceNotFoundException
import com.moviemain.core.Resource
import com.moviemain.domain.RepositoryImpl
import com.moviemain.model.data.MovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {

    /* ---------------------------Popular movies request--------------------------- */
    private val _popularList = MutableLiveData<Resource<MovieList>>()
    val popularList: LiveData<Resource<MovieList>> = _popularList

    //Downloads data from api
    fun getPopularMovies() {
        _popularList.postValue(Resource.Loading)
        viewModelScope.launch {
            try {
                val popularList = repository.getPopularMovies()
                if (popularList.results.isEmpty()) {
                    _popularList.postValue((Resource.Failure(ResourceNotFoundException())))
//                    repository.getAllMoviesFromDatabase()
                } else {
//                    repository.clearMovies()
                    _popularList.postValue(Resource.Success(popularList))
//                    repository.insertMovies(MovieEntity())
                }
            } catch (e: Exception) {
                _popularList.postValue(Resource.Failure(e))
            }
        }
    }

    /* ---------------------------Top Rated movies request--------------------------- */
    private val _topRatedList = MutableLiveData<Resource<MovieList>>()
    val topRatedList: LiveData<Resource<MovieList>> = _topRatedList

    //Downloads data from api
    fun getTopRatedMovies() {
        _topRatedList.postValue(Resource.Loading)
        viewModelScope.launch {
            try {
                val topRatedList = repository.getTopRatedMovies()
                if (topRatedList.results.isEmpty()) {
                    _topRatedList.postValue((Resource.Failure(ResourceNotFoundException())))
//                    repository.getAllMoviesFromDatabase(MovieEntity())
                } else {
//                    repository.clearMovies()
                    _topRatedList.postValue(Resource.Success(topRatedList))
//                    repository.saveMovieToLocalDataBase(MovieEntity())
                }
            } catch (e: Exception) {
                _topRatedList.postValue(Resource.Failure(e))
            }
        }
    }

    /* ---------------------------Now Playing movies request--------------------------- */
    private val _nowPlayingList = MutableLiveData<Resource<MovieList>>()
    val nowPlayingList: LiveData<Resource<MovieList>> = _nowPlayingList

    //Downloads data from api
    fun getNowPlayingMovies() {
        _nowPlayingList.postValue(Resource.Loading)
        viewModelScope.launch {
            try {
                val nowPlayingList = repository.getNowPlayingMovies()
                if (nowPlayingList.results.isEmpty()) {
                    _nowPlayingList.postValue((Resource.Failure(ResourceNotFoundException())))
//                    repository.getAllMoviesFromDatabase(MovieEntity())
                } else {
//                    repository.clearMovies()
                    _nowPlayingList.postValue(Resource.Success(nowPlayingList))
//                    repository.saveMovieToLocalDataBase(MovieEntity())
                }
            } catch (e: Exception) {
                _nowPlayingList.postValue(Resource.Failure(e))
            }
        }
    }
}

