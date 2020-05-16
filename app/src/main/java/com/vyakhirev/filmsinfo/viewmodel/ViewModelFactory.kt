package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.data.MovieDataSource

class ViewModelFactory(private val repository: MovieDataSource) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmListViewModel(repository) as T
    }
}
