package com.example.mininetflix.data.repository

import com.example.mininetflix.data.api.RetrofitClient
import com.example.mininetflix.data.db.FavoriteDao
import com.example.mininetflix.data.db.FavoriteMovie
import com.example.mininetflix.data.model.MovieResponse
import com.example.mininetflix.data.model.VideoResponse
import com.example.mininetflix.util.Constants
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val favoriteDao: FavoriteDao) {
    private val api = RetrofitClient.api
    private val apiKey = Constants.API_KEY

    suspend fun getTrendingMovies(): MovieResponse {
        return api.getTrendingToday(apiKey)
    }

    suspend fun getPopularMovies(): MovieResponse {
        return api.getPopularMovies(apiKey)
    }

    suspend fun getTopRatedMovies(): MovieResponse {
        return api.getTopRatedMovies(apiKey)
    }

    suspend fun getUpcomingMovies(): MovieResponse {
        return api.getUpcomingMovies(apiKey)
    }

    suspend fun getSimilarMovies(movieId: Int): MovieResponse {
        return api.getSimilarMovies(movieId, apiKey)
    }

    suspend fun searchMovies(query: String): MovieResponse {
        return api.searchMovies(query, apiKey)
    }

    suspend fun getMovieVideos(movieId: Int): VideoResponse {
        return api.getMovieVideos(movieId, apiKey)
    }

    // Room DB methods
    fun getAllFavorites(): Flow<List<FavoriteMovie>> {
        return favoriteDao.getAllFavorites()
    }

    suspend fun isFavorite(id: Int): Boolean {
        return favoriteDao.isFavorite(id)
    }

    suspend fun insertFavorite(movie: FavoriteMovie) {
        favoriteDao.insertFavorite(movie)
    }

    suspend fun deleteFavorite(movie: FavoriteMovie) {
        favoriteDao.deleteFavorite(movie)
    }

    suspend fun deleteFavoriteById(id: Int) {
        favoriteDao.deleteFavoriteById(id)
    }
}
