package com.moviemain.repository


import com.moviemain.model.NowPlayingList
import com.moviemain.model.PopularList
import com.moviemain.model.TopRatedList
import com.moviemain.model.UpcomingList
import com.moviemain.model.network.APIServices
import com.moviemain.model.network.API_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val APIService: APIServices) {

    suspend fun getPopularMovies(): PopularList {
        return APIService.getPopularMovies(API_KEY)
    }

    suspend fun getTopRatedMovies(): TopRatedList {
        return APIService.getTopRatedMovies(API_KEY)
    }

    suspend fun getUpcomingMovies(): UpcomingList {
        return APIService.getUpcomingMovies(API_KEY)
    }

    suspend fun getNow_PlayingMovies(): NowPlayingList {
        return APIService.getNow_PlayingMovies(API_KEY)
    }

}