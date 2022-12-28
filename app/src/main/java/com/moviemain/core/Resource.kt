package com.moviemain.core

import com.moviemain.model.data.MovieList
import retrofit2.Response

sealed class Resource<out T>() {
    object Loading : Resource<Nothing>()
    class Success<out T>(val data: T) : Resource<T>()
    class Failure(val exception: Exception) : Resource<Nothing>()
}

sealed class ResourcePaging<out T>() {
    object LoadingPaging : ResourcePaging<Nothing>()
    class SuccessPaging<out T>(val data: Response<MovieList>) : ResourcePaging<T>()
    class FailurePaging(val exception: Exception) : ResourcePaging<Nothing>()
}
