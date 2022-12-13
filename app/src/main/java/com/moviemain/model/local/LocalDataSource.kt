package com.moviemain.model.local

import com.moviemain.model.data.MovieList
import com.moviemain.model.local.dao.MovieDao
import com.moviemain.model.local.entity.MovieEntity
import com.moviemain.model.local.entity.toMovieList
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val movieDao: MovieDao) {

    suspend fun getPopularMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "popular" }.toMovieList()
    }

    suspend fun getTopRatedMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "top_rated" }.toMovieList()
    }

    suspend fun getUpcomingMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "now_playing" }.toMovieList()
    }

    suspend fun getNowPlayingMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "upcoming" }.toMovieList()
    }

    suspend fun saveMovie(movie: MovieEntity) {
        movieDao.saveMovie(movie)
    }

    suspend fun clearMovies() {
        movieDao.deleteMovie()
    }
}