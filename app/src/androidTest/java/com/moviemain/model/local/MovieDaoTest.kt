package com.moviemain.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.*
import com.moviemain.data.local.FavoritesEntity
import com.moviemain.data.local.MovieDao
import com.moviemain.data.local.MovieDatabase
import com.moviemain.data.local.MovieEntity
import com.moviemain.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MovieDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        )
            .allowMainThreadQueries().build()
        dao = database.movieDao()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testSaveFavoriteMovie() = runTest {

        val favoritesEntity = FavoritesEntity(
            76600,
            false,
            "/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
            "en",
            "Avatar: The Way of Water",
            "Ambientada más de una década después de los acontecimientos de la primera película, 'Avatar: The Way of Water' empieza contando la historia de la familia Sully (Jake, Neytiri y sus hijos), los problemas que los persiguen, lo que tienen que hacer para mantenerse a salvo, las batallas que libran para seguir con vida y las tragedias que sufren.",
            4762.653,
            "/kUAG4ZQcsNbRyiPyAr3hLdsVgAq.jpg",
            "2022-12-14",
            "Avatar: El sentido del agua",
            false,
            7.7,
            3902
        )
        dao.saveFavoriteMovie(favoritesEntity)

        val movie = dao.getAllFavoriteMoviesWithChanges().getOrAwaitValue()

        assertThat(movie).isNotEmpty()
        assertThat(movie).contains(favoritesEntity)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllFavoriteMovie() = runTest {

        val favoritesEntity = FavoritesEntity(
            76600,
            false,
            "/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
            "en",
            "Avatar: The Way of Water",
            "Ambientada más de una década después de los acontecimientos de la primera película, 'Avatar: The Way of Water' empieza contando la historia de la familia Sully (Jake, Neytiri y sus hijos), los problemas que los persiguen, lo que tienen que hacer para mantenerse a salvo, las batallas que libran para seguir con vida y las tragedias que sufren.",
            4762.653,
            "/kUAG4ZQcsNbRyiPyAr3hLdsVgAq.jpg",
            "2022-12-14",
            "Avatar: El sentido del agua",
            false,
            7.7,
            3902
        )
        val favoritesEntity2 = FavoritesEntity(
            899112,
            false,
            "/zrnzWEQSJ0jtufPGR4SEnB6s1q1.jpg",
            "en",
            "Violent Night",
            "Cuando un equipo de mercenarios irrumpe en Nochebuena dentro de un complejo familiar adinerado y toma como rehenes a todos los que están dentro, no estaban preparados para un defensor sorpresa: Santa Claus (David Harbour) está en el edificio y a punto de demostrar por qué este Santa Claus, no es ningún santo.",
            3384.094,
            "/hqBeKurpfqoV5msTH5XttGLwFUv.jpg",
            "2022-11-30",
            "Violent Night",
            false,
            7.7,
            859
        )
        dao.saveFavoriteMovie(favoritesEntity)
        dao.saveFavoriteMovie(favoritesEntity2)

        val movie = dao.getAllFavoriteMovies()

        assertThat(movie).isNotEmpty()
        assertThat(movie.size).isEqualTo(2)
        assertThat(movie).contains(favoritesEntity)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetMovieById() = runTest {

        val favoritesEntity = FavoritesEntity(
            76600,
            false,
            "/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
            "en",
            "Avatar: The Way of Water",
            "Ambientada más de una década después de los acontecimientos de la primera película, 'Avatar: The Way of Water' empieza contando la historia de la familia Sully (Jake, Neytiri y sus hijos), los problemas que los persiguen, lo que tienen que hacer para mantenerse a salvo, las batallas que libran para seguir con vida y las tragedias que sufren.",
            4762.653,
            "/kUAG4ZQcsNbRyiPyAr3hLdsVgAq.jpg",
            "2022-12-14",
            "Avatar: El sentido del agua",
            false,
            7.7,
            3902
        )
        dao.saveFavoriteMovie(favoritesEntity)

        val movie = dao.getMovieById(favoritesEntity.id)

        assertThat(movie).isNotNull()
        assertThat(movie).isEqualTo(favoritesEntity)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testDeleteFavoriteMovie() = runTest {
        val favoritesEntity = FavoritesEntity(
            76600,
            false,
            "/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
            "en",
            "Avatar: The Way of Water",
            "Ambientada más de una década después de los acontecimientos de la primera película, 'Avatar: The Way of Water' empieza contando la historia de la familia Sully (Jake, Neytiri y sus hijos), los problemas que los persiguen, lo que tienen que hacer para mantenerse a salvo, las batallas que libran para seguir con vida y las tragedias que sufren.",
            4762.653,
            "/kUAG4ZQcsNbRyiPyAr3hLdsVgAq.jpg",
            "2022-12-14",
            "Avatar: El sentido del agua",
            false,
            7.7,
            3902
        )
        dao.saveFavoriteMovie(favoritesEntity)
        dao.deleteFavoriteMovie(favoritesEntity)

        val movie = dao.getAllFavoriteMoviesWithChanges().getOrAwaitValue()

        assertThat(movie).isEmpty()
        assertThat(movie).doesNotContain(favoritesEntity)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSaveMovie() = runTest {
        val movieEntity = MovieEntity(
            76600,
            false,
            "/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
            "en",
            "Avatar: The Way of Water",
            "Ambientada más de una década después de los acontecimientos de la primera película, 'Avatar: The Way of Water' empieza contando la historia de la familia Sully (Jake, Neytiri y sus hijos), los problemas que los persiguen, lo que tienen que hacer para mantenerse a salvo, las batallas que libran para seguir con vida y las tragedias que sufren.",
            4762.653,
            "/kUAG4ZQcsNbRyiPyAr3hLdsVgAq.jpg",
            "2022-12-14",
            "Avatar: El sentido del agua",
            false,
            7.7,
            3902
        )
        dao.saveMovie(movieEntity)

        val movie = dao.getMovies("")

        assertThat(movie).isNotEmpty()
    }


    @After
    fun tearDown() {
        database.close()
    }
}