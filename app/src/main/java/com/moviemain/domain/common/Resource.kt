package com.moviemain.domain.common

/**
 * This class differs from RESULT, because the latter uses LIVEDATA differently in ViewModel
 */

/**
 * Class to wrap responses handling 3 possible states: Success, Error, Exception
 */

sealed class Resource<out T>() {
    object Loading : Resource<Nothing>()
    class Success<out T>(val data: T) : Resource<T>()
    class Failure(val exception: Exception) : Resource<Nothing>()
}

