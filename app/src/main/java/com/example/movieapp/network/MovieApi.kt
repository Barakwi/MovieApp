package com.example.movieapp.network

import com.example.movieapp.data.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMoviesByCategory(
        @Query("api_key") apiKey: String,
        @Path("category") category: String,
        @Query("page") page : Int,
        @Query("page_size") pageSize : Int = 20):
            Response<MovieResponse>
}
