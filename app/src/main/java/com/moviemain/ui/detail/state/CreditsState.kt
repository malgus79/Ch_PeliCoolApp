package com.moviemain.ui.detail.state

import com.moviemain.data.model.Credits
import com.moviemain.ui.base.ErrorState

sealed class CreditsState {
    object Loading : CreditsState()
    data class Success(val credits: Credits) : CreditsState()
    data class Failure(val error: ErrorState? = null) : CreditsState()
}