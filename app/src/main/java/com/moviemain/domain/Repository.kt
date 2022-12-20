package com.moviemain.domain

import com.moviemain.model.data.MovieList
import com.moviemain.model.local.LocalDataSource
import com.moviemain.model.local.toMovieEntity
import com.moviemain.model.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getPopularMovies(): MovieList {
        remoteDataSource.getPopularMovies().results.forEach {
            localDataSource.saveMovie(it.toMovieEntity("popular"))
        }
        return localDataSource.getPopularMovies()
    }

    suspend fun getTopRatedMovies(): MovieList {
        remoteDataSource.getTopRatedMovies().results.forEach {
            localDataSource.saveMovie(it.toMovieEntity("top_rated"))
        }
        return localDataSource.getTopRatedMovies()
    }

    suspend fun getNowPlayingMovies(): MovieList {
        remoteDataSource.getNowPlayingMovies().results.forEach {
            localDataSource.saveMovie(it.toMovieEntity("now_playing"))
        }
        return remoteDataSource.getNowPlayingMovies()
    }

    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList> {
        return remoteDataSource.getUpcomingMovies(currentPage)
    }

}