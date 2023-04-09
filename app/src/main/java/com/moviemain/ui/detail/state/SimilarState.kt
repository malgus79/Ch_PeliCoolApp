package com.moviemain.ui.detail.state

import com.moviemain.data.model.MovieList
import com.moviemain.ui.base.ErrorState

sealed class SimilarState {
    object Loading : SimilarState()
    data class Success(val similar: MovieList) : SimilarState()
    data class Failure(val error: ErrorState? = null) : SimilarState()
}