package com.moviemain.viewmodel.fragments

import com.moviemain.core.common.Constants
import com.moviemain.dataaccess.JSONFileLoader
import com.moviemain.model.data.Movie
import com.moviemain.model.remote.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryViewModelTest {

    private lateinit var apiService: ApiService

    companion object {
        const val API_KEY = "5ab6b649a24299a96dc96faa2c825afa"
        const val LANGUAGE_es_ES = "es-ES"
        const val PAGE_INDEX = 1
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
    fun `check fetch main movie upcoming is not null test`() {
        runBlocking {
            val result = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            MatcherAssert.assertThat(result.body()?.results, Matchers.`is`(Matchers.notNullValue()))
        }
    }

    @Test
    fun `check item movies upcoming for page test`() {
        runBlocking {
            val result = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            MatcherAssert.assertThat(result.body()?.results?.size, Matchers.`is`(20))
        }
    }

    @Test
    fun `check item movies upcoming adult test`() {
        runBlocking {
            val result = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            MatcherAssert.assertThat(
                result.body()?.results?.filter { Movie().adult == true },
                Matchers.`is`(emptyList())
            )
        }
    }

    @Test
    fun `check error fetch main movies upcoming test`() {
        runBlocking {
            try {
                apiService.getUpcomingMovies("", "", 0)
            } catch (e: Exception) {
                MatcherAssert.assertThat(e.localizedMessage, Matchers.`is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check movies upcoming remote with local test`() {
        runBlocking {
            val remoteResult = apiService.getUpcomingMovies(API_KEY, LANGUAGE_es_ES, PAGE_INDEX)
            val localResult = JSONFileLoader().loadMovieList("movie_response_success")

            MatcherAssert.assertThat(
                localResult?.results?.size,
                Matchers.`is`(remoteResult.body()?.results?.size)
            )

            MatcherAssert.assertThat(
                localResult?.results.isNullOrEmpty(),
                Matchers.`is`(remoteResult.body()?.results?.isEmpty())
            )

            MatcherAssert.assertThat(
                localResult?.results?.contains(Movie()),
                Matchers.`is`(remoteResult.body()?.results?.contains(Movie()))
            )

            MatcherAssert.assertThat(
                localResult?.results?.indices,
                Matchers.`is`(remoteResult.body()?.results?.indices)
            )
        }
    }
}