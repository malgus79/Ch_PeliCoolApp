package com.moviemain.ui.detail.state

import com.moviemain.data.model.Details
import com.moviemain.data.model.VideosList
import com.moviemain.ui.base.ErrorState

sealed class ButtonsState {
    object Loading : ButtonsState()
    data class SuccessHomepage(val homepage: Details) : ButtonsState()
    data class SuccessTrailer(val trailer: VideosList) : ButtonsState()
    data class Failure(val error: ErrorState? = null) : ButtonsState()
}
