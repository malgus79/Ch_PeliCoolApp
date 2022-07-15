package com.moviemain.model.network

import com.moviemain.model.data.NowPlayingList
import com.moviemain.model.data.PopularList
import com.moviemain.model.data.TopRatedList
import com.moviemain.model.data.UpcomingList
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val APIService: APIServices) {

    suspend fun getPopularMovies(): PopularList {
        return APIService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getTopRatedMovies(): TopRatedList {
        return APIService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getUpcomingMovies(): UpcomingList {
        return APIService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES)
    }

    suspend fun getNowPlayingMovies(): NowPlayingList {
        return APIService.getNow_PlayingMovies(API_KEY, LANGUAGE_es_ES)
    }

}