package com.example.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val posterPath: String?
)
