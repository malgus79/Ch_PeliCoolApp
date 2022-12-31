package com.moviemain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.moviemain.core.Constants.PAGE_INDEX
import com.moviemain.core.ResourcePaging
import com.moviemain.domain.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {

    val listData = repository.listDataRepository.cachedIn(viewModelScope)

    fun fetchUpcomingMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(ResourcePaging.LoadingPaging)
        try {
            emit(ResourcePaging.SuccessPaging(repository.getUpcomingMovies(PAGE_INDEX)))
        } catch (e: Exception) {
            emit(ResourcePaging.FailurePaging(e))
        }
    }
}

//    val listData = Pager(PagingConfig(pageSize = 1)) {
//        DataPagingSource(repository)
//    }.flow.cachedIn(viewModelScope)