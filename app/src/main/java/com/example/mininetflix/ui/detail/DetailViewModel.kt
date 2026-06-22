package com.example.mininetflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mininetflix.data.db.FavoriteMovie
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.data.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _similarMovies = MutableLiveData<List<Movie>>()
    val similarMovies: LiveData<List<Movie>> = _similarMovies

    private val _trailerKey = MutableLiveData<String?>()
    val trailerKey: LiveData<String?> = _trailerKey

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun checkFavoriteStatus(movieId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(movieId)
        }
    }

    fun fetchDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                // Fetch recommendations
                val similar = repository.getSimilarMovies(movieId).results
                _similarMovies.value = similar

                // Fetch videos
                val videos = repository.getMovieVideos(movieId).results
                // Search for YouTube trailer or fallback teaser
                val trailer = videos.firstOrNull {
                    it.site.equals("YouTube", ignoreCase = true) &&
                    (it.type.equals("Trailer", ignoreCase = true) || it.type.equals("Teaser", ignoreCase = true))
                } ?: videos.firstOrNull { it.site.equals("YouTube", ignoreCase = true) }

                _trailerKey.value = trailer?.key
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to load additional details"
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val currentlyFav = _isFavorite.value ?: false
            if (currentlyFav) {
                repository.deleteFavoriteById(movie.id)
                _isFavorite.value = false
            } else {
                val favMovie = FavoriteMovie(
                    id = movie.id,
                    title = movie.title,
                    name = movie.name,
                    overview = movie.overview,
                    posterPath = movie.posterPath,
                    backdropPath = movie.backdropPath,
                    releaseDate = movie.releaseDate,
                    firstAirDate = movie.firstAirDate,
                    voteAverage = movie.voteAverage
                )
                repository.insertFavorite(favMovie)
                _isFavorite.value = true
            }
        }
    }
}
