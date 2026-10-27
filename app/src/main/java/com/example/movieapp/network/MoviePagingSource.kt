package com.example.movieapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.Movie


class MoviePagingSource(apiService: MovieApi) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        val response = RetrofitClient.movieApi.getMoviesByCategory("107f82ed6dd4da4803ec744ace7ea6d2", "popular", page, pageSize)
        return if (response.isSuccessful && response.body() != null) {
            val movies = response.body()!!.movies
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )

        } else {
            LoadResult.Error(Exception("Failed to load movies"))

        }

    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)

    }


}