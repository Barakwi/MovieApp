package com.example.movieapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private const val API_KEY = "107f82ed6dd4da4803ec744ace7ea6d2"

    private val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val originalRequest = chain.request()
        val urlWithKey = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlWithKey)
            .build()

        chain.proceed(newRequest)
    }.build()

    val movieApi : MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Attach the interceptor here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

}