package com.moviemain.viewmodel.fragments

import com.moviemain.core.common.Constants
import com.moviemain.core.common.Constants.API_KEY
import com.moviemain.core.common.Constants.LANGUAGE_es_ES
import com.moviemain.dataaccess.JSONFileLoader
import com.moviemain.model.data.Movie
import com.moviemain.model.remote.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModelTest {

    private lateinit var apiService: ApiService

    companion object {
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
    fun `check fetch movie searched is not null test`() {
        runBlocking {
            val result = apiService.getMovieByName("", API_KEY, LANGUAGE_es_ES)
            assertThat(result?.results, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item movies searched for page test`() {
        runBlocking {
            val result = apiService.getMovieByName("", API_KEY, LANGUAGE_es_ES)
            assertThat(result?.results?.size, `is`(20))
        }
    }

    @Test
    fun `check item movies searched adult test`() {
        runBlocking {
            val result = apiService.getMovieByName("", API_KEY, LANGUAGE_es_ES)
            assertThat(
                result?.results?.filter { Movie().adult == true },
                `is`(emptyList())
            )
        }
    }

    @Test
    fun `check error fetch movies test`() {
        runBlocking {
            try {
                apiService.getMovieByName("", API_KEY, LANGUAGE_es_ES)
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check movies searched remote with local test`() {
        runBlocking {
            val remoteResult = apiService.getMovieByName("", API_KEY, LANGUAGE_es_ES)
            val localResult = JSONFileLoader().loadMovieList("movie_response_success")

            assertThat(
                localResult?.results?.size,
                `is`(remoteResult?.results?.size)
            )

            assertThat(
                localResult?.results.isNullOrEmpty(),
                `is`(remoteResult?.results?.isEmpty())
            )

            assertThat(
                localResult?.results?.contains(Movie()),
                `is`(remoteResult?.results?.contains(Movie()))
            )

            assertThat(
                localResult?.results?.indices,
                `is`(remoteResult?.results?.indices)
            )
        }
    }
}