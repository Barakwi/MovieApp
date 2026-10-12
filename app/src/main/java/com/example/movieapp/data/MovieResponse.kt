package com.example.movieapp.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val movie : List<Movie>
)
