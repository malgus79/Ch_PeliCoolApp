package com.moviemain.model.network


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org/3/movie/"
const val API_KEY = "5ab6b649a24299a96dc96faa2c825afa"
const val LANGUAGE_es_ES = "es-ES"
const val LANGUAGE_en_US = "en-US"

//Create logging interceptor
private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

//Create OkHttp Client
private val client = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)

//Create Retrofit Builder
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client.build())
    .build()

//Create retrofit instance
object MovieApi {
    val retrofitService: APIServices by lazy {
        retrofit.create(APIServices::class.java)
    }
}
