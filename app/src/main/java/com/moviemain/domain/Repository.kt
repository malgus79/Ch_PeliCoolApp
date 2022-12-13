package com.moviemain.domain

import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import com.moviemain.model.data.toDomain
import com.moviemain.model.local.LocalDataSource
import com.moviemain.model.local.entity.MovieEntity
import com.moviemain.model.network.RemoteDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getAllMoviesFromApi(): List<Movie> {
        val response: MovieList = remoteDataSource.getPopularMovies()
        return response.results.map { it.toDomain() }
    }

    suspend fun getAllMoviesFromDatabase(): List<Movie> {
        val response: MovieList = localDataSource.getPopularMovies()
        return response.results.map { it.toDomain() }
    }

    suspend fun insertMovies(movies: MovieEntity) {
        localDataSource.saveMovie(movies)
    }

    suspend fun clearMovies() {
        localDataSource.clearMovies()
    }
}