package com.example.mininetflix.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mininetflix.data.db.AppDatabase
import com.example.mininetflix.data.repository.MovieRepository
import com.example.mininetflix.ui.home.HomeViewModel
import com.example.mininetflix.ui.search.SearchViewModel
import com.example.mininetflix.ui.mylist.MyListViewModel
import com.example.mininetflix.ui.detail.DetailViewModel

class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyListViewModel::class.java) -> {
                MyListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context.applicationContext)
                val repository = MovieRepository(database.favoriteDao())
                val factory = ViewModelFactory(repository)
                instance = factory
                factory
            }
        }
    }
}
