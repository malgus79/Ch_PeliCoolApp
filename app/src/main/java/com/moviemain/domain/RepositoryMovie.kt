package com.moviemain.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.moviemain.data.local.MovieEntity
import com.moviemain.data.model.*
import com.moviemain.domain.common.Resource
import com.moviemain.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface RepositoryMovie {

    /* --------------------------- HOME --------------------------- */
    suspend fun getPopularMovies(): MovieList
    suspend fun getTopRatedMovies(): MovieList
    suspend fun getNowPlayingMovies(): MovieList

    /* --------------------------- GALLERY --------------------------- */
    suspend fun getUpcomingMovies(currentPage: Int): Result<List<Movie>>
    fun listGalleryDataRepository(): Flow<PagingData<Movie>>

    /* --------------------------- BOOKMARK --------------------------- */
    suspend fun isMovieFavorite(movie: Movie): Boolean
    suspend fun deleteFavoriteMovie(movie: Movie)
    suspend fun saveFavoriteMovie(movie: Movie)
    fun getFavoritesMovies(): LiveData<List<Movie>>

    /* --------------------------- SEARCH --------------------------- */
    suspend fun getMovieByName(movieSearched: String?): Flow<Resource<List<Movie>>>
    suspend fun getCachedMovies(movieSearched: String?): Resource<List<Movie>>
    suspend fun saveMovie(movie: MovieEntity)

    /* --------------------------- DETAIL --------------------------- */
    suspend fun getHomepage(id: Int): Result<Details>
    suspend fun getTrailerMovie(id: Int): Result<VideosList>
    suspend fun getSimilarMovie(id: Int): Result<MovieList>
    suspend fun getCreditsMovie(id: Int): Result<Credits>
}