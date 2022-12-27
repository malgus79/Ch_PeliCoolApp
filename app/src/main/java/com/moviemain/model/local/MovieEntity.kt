package com.moviemain.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moviemain.model.data.Movie
import com.moviemain.model.data.MovieList

@Entity(tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey val id: Int = -1,
    @ColumnInfo(name = "adult") val adult: Boolean = false,
    @ColumnInfo(name = "backdrop_path") val backdrop_path: String = "",
    @ColumnInfo(name = "original_title") val original_title: String = "",
    @ColumnInfo(name = "original_language") val original_language: String = "",
    @ColumnInfo(name = "overview") val overview: String = "",
    @ColumnInfo(name = "popularity") val popularity: Double = -1.0,
    @ColumnInfo(name = "poster_path") val poster_path: String = "",
    @ColumnInfo(name = "release_date") val release_date: String = "",
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "video") val video: Boolean = false,
    @ColumnInfo(name = "vote_average") val vote_average: Double = -1.0,
    @ColumnInfo(name = "vote_count") val vote_count: Int = -1,
    @ColumnInfo(name = "movie_type") var movie_type: String = ""
)

@Entity(tableName = "favorites_entity")
data class FavoritesEntity(
    @PrimaryKey val id: Int = -1,
    @ColumnInfo(name = "adult") val adult: Boolean = false,
    @ColumnInfo(name = "backdrop_path") val backdrop_path: String = "",
    @ColumnInfo(name = "original_title") val original_title: String = "",
    @ColumnInfo(name = "original_language") val original_language: String = "",
    @ColumnInfo(name = "overview") val overview: String = "",
    @ColumnInfo(name = "popularity") val popularity: Double = -1.0,
    @ColumnInfo(name = "poster_path") val poster_path: String = "",
    @ColumnInfo(name = "release_date") val release_date: String = "",
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "video") val video: Boolean = false,
    @ColumnInfo(name = "vote_average") val vote_average: Double = -1.0,
    @ColumnInfo(name = "vote_count") val vote_count: Int = -1,
    @ColumnInfo(name = "movie_type") var movie_type: String = ""
)


fun List<MovieEntity>.toMovieList(): MovieList {
    val resultList = mutableListOf<Movie>()
    this.forEach { movieEntity ->
        resultList.add(movieEntity.toMovie())
    }
    return MovieList(resultList)
}

fun MovieEntity.toMovie(): Movie = Movie(
    this.adult,
    this.backdrop_path,
    this.id,
    this.original_title,
    this.original_language,
    this.overview,
    this.popularity,
    this.poster_path,
    this.release_date,
    this.title,
    this.video,
    this.vote_average,
    this.vote_count
)

fun Movie.toMovieEntity(movieType: String): MovieEntity = MovieEntity(
    this.id,
    this.adult,
    this.backdrop_path,
    this.original_title,
    this.original_language,
    this.overview,
    this.popularity,
    this.poster_path,
    this.release_date,
    this.title,
    this.video,
    this.vote_average,
    this.vote_count,
    movie_type = movieType
)

fun List<FavoritesEntity>.asMovieList(): List<Movie> =
    this.map {
        Movie(
            it.adult,
            it.backdrop_path,
            it.id,
            it.original_title,
            it.original_language,
            it.overview,
            it.popularity,
            it.poster_path,
            it.release_date,
            it.title,
            it.video,
            it.vote_average,
            it.vote_count,
            it.movie_type
        )
    }