package com.moviemain.core

import com.moviemain.model.data.Movie


sealed class State<T>() {

    class Success<T>(val data: List<Movie>) : State<T>()
    class Failure<T>(val cause: Throwable) : State<T>()
    class Loading<T>() : State<T>()
}