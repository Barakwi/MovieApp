package com.example.movieapp.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("title")
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String?

)
