package com.moviemain.data.network

import com.moviemain.data.NowPlayingList
import com.moviemain.data.PopularList
import com.moviemain.data.TopRatedList
import com.moviemain.data.UpcomingList
import retrofit2.http.GET
import retrofit2.http.Query


interface APIServices {

    @GET("popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): PopularList

    @GET("top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String): TopRatedList

    @GET("upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String): UpcomingList

    @GET("now_playing")
    suspend fun getNow_PlayingMovies(@Query("api_key") apiKey: String): NowPlayingList
}