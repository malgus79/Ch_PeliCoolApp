package com.moviemain.model.local

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_entity")
    suspend fun getAllMovies():MovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movie:MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
}