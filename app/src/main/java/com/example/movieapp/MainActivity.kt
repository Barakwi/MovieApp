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
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.adapter.MoviePagingAdapter
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.network.MovieDao
import com.example.movieapp.network.MovieDataBase
import com.example.movieapp.network.RetrofitClient
import com.example.movieapp.viewModel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieDataBase.getDataBase(this).movieDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Insets: فوق على الـ AppBar، وتحت على الـ RecyclerView
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(left = bars.left, top = bars.top, right = bars.right)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerMovies) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = bars.bottom)
            insets
        }

        binding.recyclerMovies.layoutManager = GridLayoutManager(this, 2)
        val pagingAdapter = MoviePagingAdapter(
            onFavoriteClick = { movie -> viewModel.addToFavorites(movie) }
        )
        binding.recyclerMovies.adapter = pagingAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.moviesPager.collectLatest { pagingAdapter.submitData(it) }
                }
                launch {
                    pagingAdapter.loadStateFlow.collectLatest { states ->
                        val isLoading = states.refresh is LoadState.Loading
                        binding.progressLoading.isVisible = isLoading
                        binding.recyclerMovies.isVisible = !isLoading

                        (states.refresh as? LoadState.Error)?.let {
                            Toast.makeText(
                                this@MainActivity,
                                "Error: ${it.error.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e("PAGING_ERROR", "Pagination failed", it.error)
                        }
                    }
                }
            }
        }
    }
}

class MovieViewModelFactory(private val movieDao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(movieDao, RetrofitClient.movieApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}