package com.moviemain.viewmodel.main

import com.moviemain.core.common.Constants
import com.moviemain.domain.RepositoryImpl
import com.moviemain.model.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var apiService: ApiService
    private lateinit var repository: RepositoryImpl

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
        homeViewModel = HomeViewModel(repository)
        apiService = retrofit.create(ApiService::class.java)
    }
}