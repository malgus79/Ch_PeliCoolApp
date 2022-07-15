package com.moviemain.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    //crear una fun x cada Dao que exista
    abstract fun getMovieDao(): MovieDao

}