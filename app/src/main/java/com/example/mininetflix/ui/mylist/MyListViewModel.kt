package com.example.mininetflix.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mininetflix.data.db.FavoriteMovie
import com.example.mininetflix.data.repository.MovieRepository

class MyListViewModel(private val repository: MovieRepository) : ViewModel() {
    val favoriteMovies: LiveData<List<FavoriteMovie>> = repository.getAllFavorites().asLiveData()
}
