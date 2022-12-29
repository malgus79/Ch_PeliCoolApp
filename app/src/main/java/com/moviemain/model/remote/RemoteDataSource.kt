package com.moviemain.model.remote

import com.moviemain.core.Constants.API_KEY
import com.moviemain.core.Constants.LANGUAGE_es_ES
import com.moviemain.core.Resource
import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Response
import java.util.concurrent.Flow
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getPopularMovies(): MovieList {
        return apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getTopRatedMovies(): MovieList {
        return apiService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getNowPlayingMovies(): MovieList {
        return apiService.getNowPlayingMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList> {
        return apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, currentPage)
    }

    suspend fun getMovieByName(movieSearched: String): kotlinx.coroutines.flow.Flow<Resource<List<Movie>>> =
        callbackFlow {
            trySend(
                Resource.Success(
                    apiService.getMovieByName(movieSearched)?.results ?: listOf()
                )
            )
            awaitClose { close() }
        }
}