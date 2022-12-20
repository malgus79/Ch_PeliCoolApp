package com.moviemain.domain

import com.moviemain.model.data.MovieList
import com.moviemain.model.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
//    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getPopularMovies(): MovieList {
        return remoteDataSource.getPopularMovies()
    }

    suspend fun getTopRatedMovies(): MovieList {
        return remoteDataSource.getTopRatedMovies()
    }

    suspend fun getNowPlayingMovies(): MovieList {
        return remoteDataSource.getNowPlayingMovies()
    }

    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList> {
        return remoteDataSource.getUpcomingMovies(currentPage)
    }

}