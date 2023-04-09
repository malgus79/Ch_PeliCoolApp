package com.moviemain.data.remote

import com.moviemain.application.Constants.CREDITS
import com.moviemain.application.Constants.DETAILS
import com.moviemain.application.Constants.NOW_PLAYING
import com.moviemain.application.Constants.POPULAR
import com.moviemain.application.Constants.SEARCH
import com.moviemain.application.Constants.SIMILAR
import com.moviemain.application.Constants.TOP_RATED
import com.moviemain.application.Constants.UPCOMING
import com.moviemain.application.Constants.VIDEOS
import com.moviemain.data.model.Credits
import com.moviemain.data.model.Details
import com.moviemain.data.model.MovieList
import com.moviemain.data.model.VideosList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(POPULAR)
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): MovieList

    @GET(TOP_RATED)
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): MovieList

    @GET(NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): MovieList

    @GET(UPCOMING)
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<MovieList>

    @GET(SEARCH)
    suspend fun getMovieByName(
        @Query(value = "") movieSearched: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): MovieList?

    @GET(DETAILS)
    suspend fun getHomepage(
        @Path(value = "movie_id") movie_id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<Details>

    @GET(VIDEOS)
    suspend fun getTrailerMovie(
        @Path(value = "movie_id") movie_id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<VideosList>

    @GET(SIMILAR)
    suspend fun getSimilarMovie(
        @Path(value = "movie_id") movie_id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<MovieList>

    @GET(CREDITS)
    suspend fun getCreditsMovie(
        @Path(value = "movie_id") movie_id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<Credits>
}