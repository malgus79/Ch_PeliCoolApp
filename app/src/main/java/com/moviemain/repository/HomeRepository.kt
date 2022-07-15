package com.moviemain.repository


import com.moviemain.model.*
import com.moviemain.model.local.MovieDao
import com.moviemain.model.local.MovieDatabase
import com.moviemain.model.local.MovieEntity
import com.moviemain.model.network.APIServices
import com.moviemain.model.network.API_KEY
import com.moviemain.model.network.LANGUAGE_es_ES
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val APIService: APIServices,
    private val movieDao: MovieDao,
) {

    suspend fun getPopularMovies(): PopularList {
        return APIService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getTopRatedMovies(): TopRatedList {
        return APIService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getUpcomingMovies(): UpcomingList {
        return APIService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getNow_PlayingMovies(): NowPlayingList {
        return APIService.getNow_PlayingMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getPopularMoviesFromDatabase(): MovieEntity {
        return movieDao.getAllMovies()
    }

}