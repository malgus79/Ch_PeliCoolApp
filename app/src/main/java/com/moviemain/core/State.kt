package com.moviemain.core


sealed class State<T>()  {

    class Success<T>(val data: T) : State<T>()
    class Failure<T>(val cause: Throwable) : State<T>()
    class Loading<T>() : State<T>()
}

sealed class StateEntity<T>()  {

    class Success<T>(): State<T>()
    class Failure<T>(val cause: Throwable) : State<T>()
    class Loading<T>() : State<T>()
}