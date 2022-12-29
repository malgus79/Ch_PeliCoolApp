package com.moviemain.model.remote

import com.moviemain.model.data.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): MovieList

    @GET("top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): MovieList

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): MovieList

    @GET("upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<MovieList>

    @GET("popular")
    suspend fun getMovieByName(
        @Query(value = "") movieSearched: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): MovieList?
}