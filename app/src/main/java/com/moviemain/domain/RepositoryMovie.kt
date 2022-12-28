package com.moviemain.domain

import androidx.lifecycle.LiveData
import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import retrofit2.Response

interface RepositoryMovie {
    suspend fun getPopularMovies(): MovieList
    suspend fun getTopRatedMovies(): MovieList
    suspend fun getNowPlayingMovies(): MovieList
    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList>

    suspend fun isMovieFavorite(movie: Movie): Boolean
    suspend fun deleteFavoriteMovie(movie: Movie)
    suspend fun saveFavoriteMovie(movie: Movie)
    fun getFavoritesMovies(): LiveData<List<Movie>>  //TODO LiveData<Resource<List<Movie>>>
}