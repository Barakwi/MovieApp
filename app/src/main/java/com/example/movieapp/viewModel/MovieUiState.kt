package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movie : List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()

}
class MovieViewModel() : ViewModel(){
    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState : StateFlow<MovieUiState> = _uiState

    init {
        fetchMovie()
    }
    fun fetchMovie() {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            try {
                val response = RetrofitClient.movieApi.getMovie("107f82ed6dd4da4803ec744ace7ea6d2")
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = MovieUiState.Success(movie = response.body()!!.movie)
                }else {
                    _uiState.value = MovieUiState.Error("Server error:${response.code()}")
                }
            }catch (e : Exception){
                _uiState.value = MovieUiState.Error(e.message ?:"Failed to load the Movie")
            }
        }


    }

}

