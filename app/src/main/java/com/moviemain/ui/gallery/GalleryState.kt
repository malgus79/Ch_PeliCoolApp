package com.moviemain.ui.gallery

import com.moviemain.data.model.Movie
import com.moviemain.ui.base.ErrorState

sealed class GalleryState {
    object Loading : GalleryState()
    data class Success(val movies: List<Movie>) : GalleryState()
    data class Failure(val error: ErrorState? = null) : GalleryState()
}