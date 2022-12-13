package com.moviemain.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moviemain.model.local.dao.MovieDao
import com.moviemain.model.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    //crear una fun x cada Dao que exista
    abstract fun movieDao(): MovieDao

    companion object {

        private var INSTANCE: MovieDatabase? = null
        private var MOVIE_DATABASE_NAME = "movie_table"

        fun getDatabase(context: Context): MovieDatabase {
            INSTANCE = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java,
                MOVIE_DATABASE_NAME
            ).build()
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}