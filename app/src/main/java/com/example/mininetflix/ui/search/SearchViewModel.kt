package com.example.mininetflix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.data.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun searchMovies(query: String) {
        if (query.trim().isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.searchMovies(query)
                _searchResults.value = response.results
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Search failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
