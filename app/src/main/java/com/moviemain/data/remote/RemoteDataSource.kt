package com.moviemain.data.remote

import com.moviemain.application.Constants.API_KEY
import com.moviemain.application.Constants.LANGUAGE_es_ES
import com.moviemain.domain.common.Resource
import com.moviemain.application.Constants.PAGE_INITIAL_API
import com.moviemain.data.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getPopularMovies(): MovieList {
        return apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES, PAGE_INITIAL_API)
    }

    suspend fun getTopRatedMovies(): MovieList {
        return apiService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES,PAGE_INITIAL_API)
    }

    suspend fun getNowPlayingMovies(): MovieList {
        return apiService.getNowPlayingMovies(API_KEY, LANGUAGE_es_ES,PAGE_INITIAL_API)
    }

    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList> {
        return apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, currentPage)
    }

    suspend fun getMovieByName(movieSearched: String): Flow<Resource<List<Movie>>> =
        callbackFlow {
            trySend(
                Resource.Success(
                    apiService.getMovieByName(movieSearched, API_KEY, LANGUAGE_es_ES)?.results ?: listOf()
                )
            )
            awaitClose { close() }
        }

    suspend fun getHomepage(id: Int): Response<Details> {
        return apiService.getHomepage(id, API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getTrailerMovie(id: Int): Response<VideosList> {
        return apiService.getTrailerMovie(id, API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getSimilarMovie(id: Int): Response<MovieList> {
        return apiService.getSimilarMovie(id, API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getCreditsMovie(id: Int): Response<Credits> {
        return apiService.getCreditsMovie(id, API_KEY, LANGUAGE_es_ES)
    }
}