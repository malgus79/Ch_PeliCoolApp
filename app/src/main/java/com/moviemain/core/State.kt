package com.moviemain.core

import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import retrofit2.Response


sealed class State<T>() {

    class Success<T>(val data: T) : State<T>()
    class Failure<T>(val cause: Throwable) : State<T>()
    class Loading<T>() : State<T>()
}

sealed class StatePaging<T>() {

    class SuccessPaging<T>(val data: Response<MovieList>) : StatePaging<T>()
    class FailurePaging<T>(val cause: Throwable) : StatePaging<T>()
    class LoadingPaging<T>() : StatePaging<T>()
}
