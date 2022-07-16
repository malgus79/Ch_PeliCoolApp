package com.moviemain.di

import android.content.Context
import androidx.room.Room
import com.moviemain.model.local.MovieDao
import com.moviemain.model.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//class LocalModule {
//
//    //Inyectar la base de datos
//    @Singleton
//    @Provides
//    fun providesRoom(@ApplicationContext context: Context): MovieDatabase {
//        return Room.databaseBuilder(context,
//            MovieDatabase::class.java,
//            MOVIE_DATABASE_NAME).build()
//    }
//
//    //Inyectar el Dao
//    @Singleton
//    @Provides
//    fun providesMovieDao(moviesdb: MovieDatabase): MovieDao {
//        return moviesdb.movieDao()
//    }
//
//    companion object {
//        private const val MOVIE_DATABASE_NAME = "movie_database"
//    }
//}

object RoomModule {

    private const val MOVIE_DATABASE_NAME = "movie_database"

    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MovieDatabase::class.java, MOVIE_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesMovieDao(db: MovieDatabase) = db.movieDao()
}