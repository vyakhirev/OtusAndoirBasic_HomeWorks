package com.vyakhirev.filmsinfo.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.data.MovieDataSource
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel

class ViewModelFactory(private val repository: MovieDataSource) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmListViewModel(repository) as T
    }
}
