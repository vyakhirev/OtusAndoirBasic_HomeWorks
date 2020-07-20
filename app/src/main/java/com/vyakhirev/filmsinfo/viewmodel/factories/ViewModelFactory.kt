package com.vyakhirev.filmsinfo.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel

class ViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmListViewModel(repo) as T
    }
}
