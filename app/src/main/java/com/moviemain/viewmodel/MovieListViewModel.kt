package com.moviemain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviemain.core.ResourceNotFoundException
import com.moviemain.core.State
import com.moviemain.data.PopularList
import com.moviemain.data.TopRatedList
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
            } catch (e: Exception){
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
            } catch (e: Exception){
                _topRatedList.postValue(State.Failure(e))
            }
        }
    }

//    private val _popularList = MutableLiveData<PopularList>()
//    private val _popularStatus = MutableLiveData<ApiStatus>()
//
//    //External LiveData
//    val popularList: LiveData<PopularList> = _popularList
//    val popularStatus: LiveData<ApiStatus> = _popularStatus
//
//
//    fun getPopularMovies() {
//        _popularStatus.value = ApiStatus.LOADING
//        viewModelScope.launch {
//            try {
//                val popularList = repository.getPopularMovies()
//                if (popularList.data.isNullOrEmpty()) {
//                    _popularStatus.value = ApiStatus.ERROR
//                } else {
//                    _popularList.value = popularList
//                    _popularStatus.value = ApiStatus.DONE
//                }
//            } catch (e: Exception) {
//                _popularStatus.value = ApiStatus.ERROR
//
//            }
//        }
//    }
//    enum class ApiStatus {DONE, LOADING, ERROR  }
}

