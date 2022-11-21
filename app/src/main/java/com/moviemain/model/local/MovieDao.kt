package com.moviemain.model.local

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movieEntity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    // Alternative to delete
    @Query("DELETE FROM movieEntity")
    suspend fun deleteMovie()
}