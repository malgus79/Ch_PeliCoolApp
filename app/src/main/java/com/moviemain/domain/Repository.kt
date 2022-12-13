package com.moviemain.domain

import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import com.moviemain.model.network.RemoteDataSource
import javax.inject.Inject

class Repository @Inject constructor(
//    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getPopularMovies(): List<Movie> {
        val response: MovieList = remoteDataSource.getPopularMovies()
        return response.results
    }

    suspend fun getTopRatedMovies(): List<Movie> {
        val response: MovieList = remoteDataSource.getTopRatedMovies()
        return response.results
    }

    suspend fun getNowPlayingMovies(): List<Movie> {
        val response: MovieList = remoteDataSource.getNowPlayingMovies()
        return response.results
    }

    suspend fun getUpcomingMovies(): List<Movie> {
        val response: MovieList = remoteDataSource.getUpcomingMovies()
        return response.results
    }

}