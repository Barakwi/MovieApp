package com.example.movieapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.viewModel.MovieUiState
import com.example.movieapp.viewModel.MovieViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerMovies.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerMovies.adapter = MovieAdapter(emptyList())

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is MovieUiState.Loading -> {
                        binding.progressLoading.isVisible = true
                        binding.recyclerMovies.isVisible = true
                        binding.textAppTitle.isVisible = false
                    }
                    is MovieUiState.Success -> {
                        binding.progressLoading.isVisible = false
                        binding.recyclerMovies.isVisible = true
                        binding.textAppTitle.isVisible = true

                        val adapter = MovieAdapter(state.movie)

                        binding.recyclerMovies.adapter = adapter

                    }
                    is MovieUiState.Error -> {
                        binding.progressLoading.isVisible = false
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }


    }
}