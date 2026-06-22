package com.example.mininetflix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> = _trendingMovies

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies

    private val _heroMovie = MutableLiveData<Movie>()
    val heroMovie: LiveData<Movie> = _heroMovie

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchHomeData() {
        viewModelScope.launch {
            try {
                val trending = repository.getTrendingMovies().results
                _trendingMovies.value = trending

                // Pick a random trending movie as the Hero banner
                if (trending.isNotEmpty()) {
                    _heroMovie.value = trending.shuffled().first()
                }

                _popularMovies.value = repository.getPopularMovies().results
                _topRatedMovies.value = repository.getTopRatedMovies().results
                _upcomingMovies.value = repository.getUpcomingMovies().results
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to fetch movies"
            }
        }
    }
}
