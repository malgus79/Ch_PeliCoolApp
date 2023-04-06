package com.moviemain.ui.base

import androidx.lifecycle.ViewModel
import com.moviemain.R
import com.moviemain.domain.common.Error

open class BaseViewModel : ViewModel() {

    fun handleError(error: Error): ErrorState {
        return when (error) {
            is Error.Connectivity -> ErrorState(
                errorMessage = R.string.connectivity_error,
            )
            is Error.HttpException -> ErrorState(
                errorMessage = error.messageResId,
            )
            is Error.Unknown -> ErrorState(
                errorMessage = R.string.unknown_error,
            )
            is Error.InternetConnection -> ErrorState(
                errorMessage = R.string.internet_error,
            )
            is Error.NotFoundMovie -> ErrorState(
                errorMessage = error.messageResId
            )
        }
    }
}