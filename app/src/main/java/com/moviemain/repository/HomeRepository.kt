package com.moviemain.repository


import com.moviemain.data.PopularList
import com.moviemain.data.network.APIServices
import com.moviemain.data.network.API_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val APIService: APIServices) {

    suspend fun getPopularMovies(): PopularList {
        return APIService.getPopularMovies(API_KEY)
    }

}