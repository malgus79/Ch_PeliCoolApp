package com.moviemain.model.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import com.moviemain.model.data.asFavoriteEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val movieDao: MovieDao) {

    suspend fun getPopularMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "popular" }.toMovieList()
    }

    suspend fun getTopRatedMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "top_rated" }.toMovieList()
    }

    suspend fun getNowPlayingMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "now_playing" }.toMovieList()
    }

    suspend fun saveMovie(movie: MovieEntity) {
        movieDao.saveMovie(movie)
    }

    suspend fun isMovieFavorite(movie: Movie): Boolean {
        return movieDao.getMovieById(movie.id) != null
    }

    suspend fun deleteMovie(movie: Movie) {
        return  movieDao.deleteFavoriteMovie(movie.asFavoriteEntity())

    }

    suspend fun saveFavoriteMovie(movie: Movie) {
        return movieDao.saveFavoriteMovie(movie.asFavoriteEntity())
    }

    fun getFavoritesMovies(): LiveData<List<Movie>> {
        return movieDao.getAllFavoriteMoviesWithChanges().map {it.asMovieList() }
    }
}