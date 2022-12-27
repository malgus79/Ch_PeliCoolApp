package com.moviemain.domain

import androidx.lifecycle.LiveData
import com.moviemain.core.CheckInternet
import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import com.moviemain.model.local.LocalDataSource
import com.moviemain.model.local.toMovieEntity
import com.moviemain.model.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : RepoMovie {

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
            remoteDataSource.getNowPlayingMovies().results.forEach {
                localDataSource.saveMovie(it.toMovieEntity("now_playing"))
            }
            localDataSource.getNowPlayingMovies()
        } else {
            localDataSource.getNowPlayingMovies()
        }
    }

    override suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList> {
        return remoteDataSource.getUpcomingMovies(currentPage)
    }

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
}