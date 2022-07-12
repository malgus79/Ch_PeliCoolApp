package com.moviemain.data.network

import com.moviemain.data.PopularList
import retrofit2.http.GET
import retrofit2.http.Query


interface APIServices {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): PopularList

}