package com.moviemain.model.data

import com.moviemain.model.local.entity.MovieEntity

data class Movie(
    val adult: Boolean = false,
    val backdrop_path: String = "",
    val id: Int = -1,
    val original_title: String = "",
    val original_language: String = "",
    val overview: String = "",
    val popularity: Double = -1.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = -1.0,
    val vote_count: Int = -1,
    var movie_type: String = "",
)

data class MovieList(val results: List<Movie> = listOf())
//
//fun Movie.toDomain() = Movie(adult,
//    backdrop_path,
//    id,
//    original_title,
//    original_language,
//    overview,
//    popularity,
//    poster_path,
//    release_date,
//    title,
//    video,
//    vote_average,
//    vote_count,
//    movie_type)
//
//fun MovieEntity.toDomain() = Movie(adult,
//    backdrop_path,
//    id,
//    original_title,
//    original_language,
//    overview,
//    popularity,
//    poster_path,
//    release_date,
//    title,
//    video,
//    vote_average,
//    vote_count,
//    movie_type)