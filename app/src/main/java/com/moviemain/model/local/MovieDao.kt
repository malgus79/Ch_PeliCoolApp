package com.moviemain.model.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_entity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieEntity)

    @Query("SELECT * FROM favorites_entity WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): FavoritesEntity?

    @Delete
    suspend fun deleteFavoriteMovie(favorites: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteMovie(movie: FavoritesEntity)

    @Query("SELECT * FROM favorites_entity")
    fun getAllFavoriteMoviesWithChanges(): LiveData<List<FavoritesEntity>>

    @Query("SELECT * FROM movie_entity WHERE original_title LIKE '%' || :movieSearched || '%'") // This Like operator is needed due that the API returns blank spaces in the name
    suspend fun getMovies(movieSearched: String?): List<MovieEntity>

}