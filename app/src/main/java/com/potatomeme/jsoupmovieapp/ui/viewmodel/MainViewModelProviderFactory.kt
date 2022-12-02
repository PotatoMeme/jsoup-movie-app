package com.potatomeme.jsoupmovieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.potatomeme.jsoupmovieapp.data.repository.MovieRepository

class MainViewModelProviderFactory(
    private val movieRepository: MovieRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(movieRepository) as T
        }
        throw java.lang.IllegalArgumentException("ViewModel class not found")
    }
}