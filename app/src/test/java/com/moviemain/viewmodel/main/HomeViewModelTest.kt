package com.moviemain.viewmodel.main

import com.moviemain.core.common.Constants
import com.moviemain.domain.RepositoryImpl
import com.moviemain.model.data.Movie
import com.moviemain.model.local.LocalDataSource
import com.moviemain.model.local.MovieDao
import com.moviemain.model.remote.ApiService
import com.moviemain.model.remote.RemoteDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var apiService: ApiService

    companion object {
        const val API_KEY = "5ab6b649a24299a96dc96faa2c825afa"
        const val LANGUAGE_es_ES = "es-ES"
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommin() {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
    }

    @Before
    fun setup() {
        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    fun checkFetchMainMoviesPopularIsNotNullTest() {
        runBlocking {
            val result = apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
            assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun checkFetchMainMoviesTopRatedIsNotNullTest() {
        runBlocking {
            val result = apiService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES)
            assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun checkFetchMainMoviesNowPlayingIsNotNullTest() {
        runBlocking {
            val result = apiService.getNowPlayingMovies(API_KEY, LANGUAGE_es_ES)
            assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun checkFetchMainMovieUpcomingIsNotNullTest() {
        runBlocking {
            val result = apiService.getNowPlayingMovies(API_KEY, LANGUAGE_es_ES)
            assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun checkItemMoviesForPageTest() {
        runBlocking {
            val result = apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
            assertThat(result.results.size, `is`(20))
        }
    }

}