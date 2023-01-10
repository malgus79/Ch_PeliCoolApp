package com.moviemain.viewmodel.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moviemain.core.common.Constants
import com.moviemain.core.common.Constants.API_KEY
import com.moviemain.core.common.Constants.LANGUAGE_es_ES
import com.moviemain.dataaccess.JSONFileLoader
import com.moviemain.model.data.Movie
import com.moviemain.model.remote.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModelTest {

    @get:Rule
    val instantExcecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutinesRule = MainCoroutineRule()

    private lateinit var apiService: ApiService

    companion object {
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon() {
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
    fun `check fetch movie popular is not null test`() {
        runBlocking {
            val result = apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
            MatcherAssert.assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun `check fetch movie top rated is not null test`() {
        runBlocking {
            val result = apiService.getTopRatedMovies(API_KEY, LANGUAGE_es_ES)
            MatcherAssert.assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun `check fetch movie now playing is not null test`() {
        runBlocking {
            val result = apiService.getNowPlayingMovies(API_KEY, LANGUAGE_es_ES)
            MatcherAssert.assertThat(result.results, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item movies for page test`() {
        runBlocking {
            val result = apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
            MatcherAssert.assertThat(result.results.size, `is`(20))
        }
    }

    @Test
    fun `check item movies adult test`() {
        runBlocking {
            val result = apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
            MatcherAssert.assertThat(
                result.results.filter { Movie().adult == true },
                `is`(emptyList())
            )
        }
    }

    @Test
    fun `check error fetch movies popular test`() {
        runBlocking {
            try {
                apiService.getPopularMovies("", "")
            } catch (e: Exception) {
                MatcherAssert.assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check error fetch movies top rated test`() {
        runBlocking {
            try {
                apiService.getTopRatedMovies("", "")
            } catch (e: Exception) {
                MatcherAssert.assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check error fetch movies now playing test`() {
        runBlocking {
            try {
                apiService.getNowPlayingMovies("", "")
            } catch (e: Exception) {
                MatcherAssert.assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check remote with local test`() {
        runBlocking {
            val remoteResult = apiService.getPopularMovies(API_KEY, LANGUAGE_es_ES)
            val localResult = JSONFileLoader().loadMovieList("movie_response_success")

            MatcherAssert.assertThat(
                localResult?.results?.size,
                `is`(remoteResult.results.size)
            )

            MatcherAssert.assertThat(
                localResult?.results.isNullOrEmpty(),
                `is`(remoteResult.results.isEmpty())
            )

            MatcherAssert.assertThat(
                localResult?.results?.contains(Movie()),
                `is`(remoteResult.results.contains(Movie()))
            )

            MatcherAssert.assertThat(
                localResult?.results?.indices,
                `is`(remoteResult.results.indices)
            )
        }
    }
}