package com.moviemain.viewmodel.fragments

import com.moviemain.core.common.Constants
import com.moviemain.core.common.Constants.API_KEY
import com.moviemain.core.common.Constants.LANGUAGE_es_ES
import com.moviemain.core.common.Constants.PAGE_INDEX
import com.moviemain.dataaccess.JSONFileLoader
import com.moviemain.model.data.Movie
import com.moviemain.model.remote.ApiService
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryViewModelTest {

    private lateinit var apiService: ApiService

    companion object {
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon() {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @Before
    fun setup() {
        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    fun `check fetch movie upcoming is not null test`() {
        runBlocking {
            val result = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            assertThat(result.body()?.results, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item movies upcoming for page test`() {
        runBlocking {
            val result = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            assertThat(result.body()?.results?.size, `is`(20))
        }
    }

    @Test
    fun `check item movies upcoming adult test`() {
        runBlocking {
            val result = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            assertThat(
                result.body()?.results?.filter { Movie().adult == true },
                `is`(emptyList())
            )
        }
    }

    @Test
    fun `check error fetch movies upcoming test`() {
        runBlocking {
            try {
                apiService.getUpcomingMovies("", "", 0)
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check movies upcoming remote with local test`() {
        runBlocking {
            val remoteResult = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            val localResult = JSONFileLoader().loadMovieList("movie_response_success")

            assertThat(
                localResult?.results?.size,
                `is`(remoteResult.body()?.results?.size)
            )

            assertThat(
                localResult?.results.isNullOrEmpty(),
                `is`(remoteResult.body()?.results?.isEmpty())
            )

            assertThat(
                localResult?.results?.contains(Movie()),
                `is`(remoteResult.body()?.results?.contains(Movie()))
            )

            assertThat(
                localResult?.results?.indices,
                `is`(remoteResult.body()?.results?.indices)
            )
        }
    }
}