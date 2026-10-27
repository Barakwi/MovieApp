package com.example.movieapp.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    val page : Int,
    @SerializedName("total_pages")
    val totalPages : Int,
    @SerializedName("results")
    val movies : List<Movie>
)
