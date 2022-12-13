package com.moviemain.model.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moviemain.model.local.dao.MovieDao
import com.moviemain.model.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}