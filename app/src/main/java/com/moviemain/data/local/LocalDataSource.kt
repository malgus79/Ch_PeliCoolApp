package com.moviemain.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.moviemain.domain.common.Resource
import com.moviemain.data.model.Movie
import com.moviemain.data.model.MovieList
import com.moviemain.data.model.asFavoriteEntity
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

    suspend fun getUpcomingMovies() : MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "upcoming" }.toMovieList()
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
        return movieDao.getAllFavoriteMoviesWithChanges().map { it.asMovieList() }
    }

    suspend fun getCachedMovies(movieSearched: String?): Resource<List<Movie>> {
        return Resource.Success(movieDao.getMovies(movieSearched).asMovieList())
    }

    suspend fun deleteCachedMovie() {
        return movieDao.deleteCachedMovie()
    }
}