package com.moviemain.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moviemain.model.data.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}