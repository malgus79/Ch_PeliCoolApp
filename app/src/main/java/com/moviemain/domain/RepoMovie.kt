package com.moviemain.domain

import com.moviemain.model.data.MovieList
import retrofit2.Response

interface RepoMovie {
    suspend fun getPopularMovies(): MovieList
    suspend fun getTopRatedMovies(): MovieList
    suspend fun getNowPlayingMovies(): MovieList
    suspend fun getUpcomingMovies(currentPage: Int): Response<MovieList>
}