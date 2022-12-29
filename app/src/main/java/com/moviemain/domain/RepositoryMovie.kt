package com.moviemain.domain

import androidx.lifecycle.LiveData
import com.moviemain.core.Resource
import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import com.moviemain.model.local.MovieEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryMovie {
    suspend fun getPopularMovies(): MovieList
    suspend fun getTopRatedMovies(): MovieList
    suspend fun getNowPlayingMovies(): MovieList
    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList>

    suspend fun isMovieFavorite(movie: Movie): Boolean
    suspend fun deleteFavoriteMovie(movie: Movie)
    suspend fun saveFavoriteMovie(movie: Movie)
    fun getFavoritesMovies(): LiveData<List<Movie>>

    suspend fun getMovieByName(movieSearched: String?): Flow<Resource<List<Movie>>>
    suspend fun getCachedMovies(movieSearched: String?): Resource<List<Movie>>
    suspend fun saveMovie(movie: MovieEntity)
}