package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.movieapp.data.FavoriteMovie
import com.example.movieapp.data.Movie
import com.example.movieapp.network.MovieApi
import com.example.movieapp.network.MovieDao
import com.example.movieapp.network.MoviePagingSource
import com.example.movieapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movie : List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()

}
class MovieViewModel(
    val movieDao: MovieDao,
    private val apiService: MovieApi):
    ViewModel(){
    val moviesPager = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            MoviePagingSource(apiService = apiService)
        }
    ).flow.cachedIn(viewModelScope)


        private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState : StateFlow<MovieUiState> = _uiState

    init {
        fetchMovie()
    }
    fun fetchMovie(category: String? = null) {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading


            try {
                val response = RetrofitClient.movieApi.getMoviesByCategory("107f82ed6dd4da4803ec744ace7ea6d2", category ?: "popular",page = 1)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = MovieUiState.Success(movie = response.body()!!.movies)
                }else {
                    _uiState.value = MovieUiState.Error("Server error:${response.code()}")
                }
            }catch (e : Exception){
                _uiState.value = MovieUiState.Error(e.message ?:"Failed to load the Movie")
            }
        }

    }
    fun addToFavorites(movie: Movie) {
        viewModelScope.launch {
            try {
                val movieEntity = FavoriteMovie(
                    id = movie.id,
                    title = movie.title,
                    posterPath = movie.posterPath
                )
                movieDao.insertMovie(movieEntity)

            } catch (e: Exception) {
                Log.e("MovieViewModel", "Failed to add movie to favorites", e)
            }
        }
    }
}

