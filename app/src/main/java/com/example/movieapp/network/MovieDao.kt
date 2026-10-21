package com.example.movieapp.network

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.FavoriteMovie
import com.example.movieapp.data.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies")
     fun getAllMovies(): Flow<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    suspend fun getMovieById(id: Int): FavoriteMovie?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovie)
    @Delete
    suspend fun deleteMovie(movie: FavoriteMovie)

}