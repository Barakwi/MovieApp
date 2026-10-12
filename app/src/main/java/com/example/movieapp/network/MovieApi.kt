package com.example.movieapp.network

import com.example.movieapp.data.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getMovie(@Query("api_key") apiKey: String): Response<MovieResponse>
}