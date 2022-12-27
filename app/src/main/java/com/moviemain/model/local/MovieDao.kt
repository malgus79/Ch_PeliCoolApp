package com.moviemain.model.local

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
}