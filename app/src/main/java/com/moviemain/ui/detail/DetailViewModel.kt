package com.moviemain.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moviemain.data.model.Movie
import com.moviemain.domain.RepositoryMovie
import com.moviemain.domain.common.fold
import com.moviemain.domain.common.toError
import com.moviemain.domain.common.validateHttpErrorCode
import com.moviemain.ui.base.BaseViewModel
import com.moviemain.ui.detail.state.ButtonsState
import com.moviemain.ui.detail.state.CreditsState
import com.moviemain.ui.detail.state.SimilarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RepositoryMovie) :
    BaseViewModel() {

    /* --------------------------- FAVORITES --------------------------- */
    fun saveOrDeleteFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            if (repository.isMovieFavorite(movie)) {
                repository.deleteFavoriteMovie(movie)
            } else {
                repository.saveFavoriteMovie(movie)
            }
        }
    }

    suspend fun isMovieFavorite(movie: Movie): Boolean =
        repository.isMovieFavorite(movie)


    /* --------------------------- BUTTONS --------------------------- */
    private val _buttonsState = MutableLiveData<ButtonsState>()
    val buttonsState: LiveData<ButtonsState> = _buttonsState

    fun fetchHomepageAndTrailerMovie(id: Int) {
        _buttonsState.value = ButtonsState.Loading
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {

            repository.getHomepage(id).fold(
                onSuccess = {
                    _buttonsState.postValue(ButtonsState.SuccessHomepage(it))
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _buttonsState.postValue(ButtonsState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _buttonsState.postValue(ButtonsState.Failure(error))
                }
            )

            repository.getTrailerMovie(id).fold(
                onSuccess = {
                    _buttonsState.postValue(ButtonsState.SuccessTrailer(it))
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _buttonsState.postValue(ButtonsState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _buttonsState.postValue(ButtonsState.Failure(error))
                }
            )
        }
    }

    /* --------------------------- SIMILAR --------------------------- */
    private val _similarState = MutableLiveData<SimilarState>()
    val similarState: LiveData<SimilarState> = _similarState

    fun fetchSimilarMovies(id: Int) {
        _similarState.value = SimilarState.Loading
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.getSimilarMovie(id).fold(
                onSuccess = {
                    _similarState.postValue(SimilarState.Success(it))
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _similarState.postValue(SimilarState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _similarState.postValue(SimilarState.Failure(error))
                }
            )
        }
    }

    /* --------------------------- CREDITS --------------------------- */
    private val _creditsState = MutableLiveData<CreditsState>()
    val creditsState: LiveData<CreditsState> = _creditsState

    fun fetchCreditsMovie(id: Int) {
        _creditsState.value = CreditsState.Loading
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.getCreditsMovie(id).fold(
                onSuccess = {
                    _creditsState.postValue(CreditsState.Success(it))
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _creditsState.postValue(CreditsState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _creditsState.postValue(CreditsState.Failure(error))
                }
            )
        }
    }
}