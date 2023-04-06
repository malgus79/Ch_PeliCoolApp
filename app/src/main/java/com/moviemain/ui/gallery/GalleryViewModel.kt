package com.moviemain.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.moviemain.application.Constants.PAGE_INDEX
import com.moviemain.domain.RepositoryMovie
import com.moviemain.domain.common.fold
import com.moviemain.domain.common.toError
import com.moviemain.domain.common.validateHttpErrorCode
import com.moviemain.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: RepositoryMovie) :
    BaseViewModel() {

    val listData = repository.listGalleryDataRepository().cachedIn(viewModelScope)

    private val _galleryMovieState = MutableLiveData<GalleryState>()
    val galleryMovieState: LiveData<GalleryState> = _galleryMovieState

    fun fetchUpcomingMovies() {
        _galleryMovieState.value = GalleryState.Loading
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.getUpcomingMovies(PAGE_INDEX).fold(
                onSuccess = {
                    _galleryMovieState.postValue(GalleryState.Success(it))
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _galleryMovieState.postValue(GalleryState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _galleryMovieState.postValue(GalleryState.Failure(error))
                }
            )
        }
    }
}

//    fun fetchUpcomingMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
//        emit(ResourcePaging.LoadingPaging)
//        try {
//            emit(ResourcePaging.SuccessPaging(repository.getUpcomingMovies(PAGE_INDEX)))
//        } catch (e: Exception) {
//            emit(ResourcePaging.FailurePaging(e))
//        }
//    }

//    val listData = Pager(PagingConfig(pageSize = 1)) {
//        DataPagingSource(repository)
//    }.flow.cachedIn(viewModelScope)