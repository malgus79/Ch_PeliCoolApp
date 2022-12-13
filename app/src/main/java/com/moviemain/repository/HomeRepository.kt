package com.moviemain.repository

import com.moviemain.core.Constants.API_KEY
import com.moviemain.core.Constants.LANGUAGE_es_ES
import com.moviemain.model.data.MovieList
import com.moviemain.model.local.MovieDao
import com.moviemain.model.local.MovieEntity
import com.moviemain.model.network.APIServices
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val APIService: APIServices,
//    private val movieDao: MovieDao,
) {

    suspend fun getPopularMovies(): MovieList {
        return APIService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getTopRatedMovies(): MovieList {
        return APIService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getUpcomingMovies(): MovieList {
        return APIService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getNowPlayingMovies(): MovieList {
        return APIService.getNowPlayingMovies(API_KEY, LANGUAGE_es_ES)
    }

//    suspend fun saveMovieToLocalDataBase(movie: MovieEntity) {
//        movieDao.saveMovie(movie)
//    }
//
//    suspend fun getAllMoviesFromDatabase(movie: MovieEntity) {
//        movieDao.getAllMovies()
//    }
//
//    suspend fun clearMovies() {
//        movieDao.deleteMovie()
//    }
}