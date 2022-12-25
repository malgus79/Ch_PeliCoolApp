package com.moviemain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.moviemain.core.Constants.PAGE_INDEX
import com.moviemain.core.ResourceNotFoundException
import com.moviemain.core.ResourcePaging
import com.moviemain.domain.RepositoryImpl
import com.moviemain.model.data.MovieList
import com.moviemain.model.paging.DataPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GalleryFragmentViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {

    private val _movieUpcomingList = MutableLiveData<ResourcePaging<Response<MovieList>>>()
    val movieUpcomingList: LiveData<ResourcePaging<Response<MovieList>>> = _movieUpcomingList

    //Downloads data from api with repository
    fun getUpcomingMovies() {
        _movieUpcomingList.postValue(ResourcePaging.LoadingPaging)
        viewModelScope.launch {
            try {
                val movieList = repository.getUpcomingMovies(PAGE_INDEX)
                if (movieList.body()?.results.isNullOrEmpty()) {
                    _movieUpcomingList.postValue(ResourcePaging.FailurePaging(ResourceNotFoundException()))
                } else {
                    _movieUpcomingList.postValue(ResourcePaging.SuccessPaging(movieList))
                }
            } catch (e: Exception) {
                _movieUpcomingList.postValue(ResourcePaging.FailurePaging(e))
            }
        }
    }

    val listData = Pager(PagingConfig(pageSize = 1)) {
        DataPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}