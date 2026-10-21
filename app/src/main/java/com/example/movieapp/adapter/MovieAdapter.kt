package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.data.Movie

class MovieAdapter(
    private val movies: List<Movie>,
    private val onFavoriteClick: (Movie) -> Unit
) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding)
    }
    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        val currentMovie = movies[position]
        holder.binding.textTitle.text = currentMovie.title

        val imageUrl = "https://image.tmdb.org/t/p/w500${currentMovie.posterPath}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.imagePoster)


        holder.binding.imageFavorite.setOnClickListener {
            onFavoriteClick(currentMovie)
        }

    }



    override fun getItemCount(): Int {

        return movies.size
    }
    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root){

    }

}