package com.moviemain.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.moviemain.application.Constants.PAGE_INDEX
import com.moviemain.core.connectivity.CheckInternet
import com.moviemain.data.local.LocalDataSource
import com.moviemain.data.local.MovieEntity
import com.moviemain.data.local.toMovieEntity
import com.moviemain.data.model.*
import com.moviemain.data.paging.DataPagingSource
import com.moviemain.data.remote.RemoteDataSource
import com.moviemain.domain.RepositoryMovie
import com.moviemain.domain.common.Resource
import com.moviemain.domain.common.Result
import com.moviemain.domain.common.fold
import com.moviemain.domain.common.makeSafeRequest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : RepositoryMovie {

    override suspend fun getPopularMovies(): MovieList {
        return if (CheckInternet.isNetworkAvailable()) {
            remoteDataSource.getPopularMovies().results.forEach {
                localDataSource.saveMovie(it.toMovieEntity("popular"))
            }
            localDataSource.getPopularMovies()
        } else {
            localDataSource.getPopularMovies()
        }
    }

    override suspend fun getTopRatedMovies(): MovieList {
        return if (CheckInternet.isNetworkAvailable()) {
            remoteDataSource.getTopRatedMovies().results.forEach {
                localDataSource.saveMovie(it.toMovieEntity("top_rated"))
            }
            localDataSource.getTopRatedMovies()
        } else {
            localDataSource.getTopRatedMovies()
        }
    }

    override suspend fun getNowPlayingMovies(): MovieList {
        return if (CheckInternet.isNetworkAvailable()) {
            localDataSource.deleteCachedMovie()  //delete the database at the beginning of the request only in this order because of the concatAdapter

            remoteDataSource.getNowPlayingMovies().results.forEach {
                localDataSource.saveMovie(it.toMovieEntity("now_playing"))
            }
            localDataSource.getNowPlayingMovies()
        } else {
            localDataSource.getNowPlayingMovies()
        }
    }

    override suspend fun getUpcomingMovies(currentPage: Int): Result<List<Movie>> {
        val response = makeSafeRequest { remoteDataSource.getUpcomingMovies(currentPage) }
        return response.fold(
            onSuccess = {
                Result.Success(it.results)
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }

    override fun listGalleryDataRepository(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(PAGE_INDEX),
    ) {
        DataPagingSource(repository = RepositoryImpl(localDataSource, remoteDataSource))
    }.flow

    override suspend fun isMovieFavorite(movie: Movie): Boolean {
        return localDataSource.isMovieFavorite(movie)
    }

    override suspend fun deleteFavoriteMovie(movie: Movie) {
        localDataSource.deleteMovie(movie)
    }

    override suspend fun saveFavoriteMovie(movie: Movie) {
        localDataSource.saveFavoriteMovie(movie)
    }

    override fun getFavoritesMovies(): LiveData<List<Movie>> {
        return localDataSource.getFavoritesMovies()
    }

    override suspend fun getMovieByName(movieSearched: String?): Flow<Resource<List<Movie>>> =
        callbackFlow {
            trySend(getCachedMovies(movieSearched))

            remoteDataSource.getMovieByName(movieSearched.toString()).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        for (movie in it.data) {
                            saveMovie(movie.asMovieEntity())
                        }
                        trySend(getCachedMovies(movieSearched))
                    }
                    is Resource.Failure -> {
                        trySend(getCachedMovies(movieSearched))
                    }
                    else -> {}
                }
            }
            awaitClose { cancel() }
        }

    override suspend fun getCachedMovies(movieSearched: String?): Resource<List<Movie>> {
        return localDataSource.getCachedMovies(movieSearched)
    }

    override suspend fun saveMovie(movie: MovieEntity) {
        localDataSource.saveMovie(movie)
    }

    override suspend fun getHomepage(id: Int): Result<Details> {
        val response = makeSafeRequest { remoteDataSource.getHomepage(id) }
        return response.fold(
            onSuccess = {
                Result.Success(it)
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }

    override suspend fun getTrailerMovie(id: Int): Result<VideosList> {
        val response = makeSafeRequest { remoteDataSource.getTrailerMovie(id) }
        return response.fold(
            onSuccess = {
                Result.Success(it)
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }

    override suspend fun getSimilarMovie(id: Int): Result<MovieList> {
        val response = makeSafeRequest { remoteDataSource.getSimilarMovie(id) }
        return response.fold(
            onSuccess = {
                Result.Success(it)
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }

    override suspend fun getCreditsMovie(id: Int): Result<Credits> {
        val response = makeSafeRequest { remoteDataSource.getCreditsMovie(id) }
        return response.fold(
            onSuccess = {
                Result.Success(it)
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }
}