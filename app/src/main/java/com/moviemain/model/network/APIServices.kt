package com.moviemain.model.network

import com.moviemain.model.NowPlayingList
import com.moviemain.model.PopularList
import com.moviemain.model.TopRatedList
import com.moviemain.model.UpcomingList
import retrofit2.http.GET
import retrofit2.http.Query


interface APIServices {

    @GET("popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String, @Query("language") language: String): PopularList

    @GET("top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String): TopRatedList

    @GET("upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String, @Query("language") language: String): UpcomingList

    @GET("now_playing")
    suspend fun getNow_PlayingMovies(@Query("api_key") apiKey: String, @Query("language") language: String): NowPlayingList
}