package com.moviemain.data.network

import com.moviemain.data.PopularList
import com.moviemain.data.TopRatedList
import com.moviemain.data.UpcomingList
import retrofit2.http.GET
import retrofit2.http.Query


interface APIServices {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): PopularList

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String): TopRatedList

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(@Query("api_key") apiKey: String): UpcomingList
}