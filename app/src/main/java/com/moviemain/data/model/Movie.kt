package com.moviemain.model.data

import android.os.Parcelable
import com.moviemain.data.local.FavoritesEntity
import com.moviemain.data.local.MovieEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val id: Int? = -1,
    val original_title: String? = "",
    val original_language: String? = "",
    val overview: String? = "",
    val popularity: Double? = -1.0,
    val poster_path: String? = "",
    val release_date: String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    val vote_average: Double? = -1.0,
    val vote_count: Int? = -1,
    var movie_type: String? = "",
) : Parcelable

data class MovieList(val results: List<Movie> = listOf())


fun Movie.asFavoriteEntity(): FavoritesEntity = FavoritesEntity(
    this.id,
    this.adult,
    this.backdrop_path,
    this.title,
    this.original_language,
    this.overview,
    this.popularity,
    this.poster_path,
    this.release_date,
    this.original_title,
    this.video,
    this.vote_average,
    this.vote_count,
    this.movie_type,
)

fun Movie.asMovieEntity(): MovieEntity = MovieEntity(
    this.id,
    this.adult,
    this.backdrop_path,
    this.title,
    this.original_language,
    this.overview,
    this.popularity,
    this.poster_path,
    this.release_date,
    this.original_title,
    this.video,
    this.vote_average,
    this.vote_count,
    this.movie_type,
)