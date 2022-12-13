package com.moviemain.model.local.dao

import androidx.room.*
import com.moviemain.model.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_entity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieEntity)

    @Query("DELETE FROM movie_entity")
    suspend fun deleteMovie()

/*
    // Alternative to delete
    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
*/
}