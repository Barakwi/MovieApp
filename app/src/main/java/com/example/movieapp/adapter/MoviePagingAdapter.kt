package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.ItemMovieBinding


class MoviePagingAdapter(
    private val onFavoriteClick: (Movie) -> Unit
) :
    PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(MovieDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviePagingAdapter.MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MoviePagingAdapter.MovieViewHolder,
        position: Int
    ) {
        val currentMovie = getItem(position)
        if (currentMovie != null) {
            holder.binding.textTitle.text = currentMovie.title
            val imageUrl = "https://image.tmdb.org/t/p/w500${currentMovie.posterPath}"
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.binding.imagePoster)
            holder.binding.imageFavorite.setOnClickListener {
                onFavoriteClick(currentMovie)
            }
        }
    }
    class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root){
    }
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem == newItem
        }
    }
}