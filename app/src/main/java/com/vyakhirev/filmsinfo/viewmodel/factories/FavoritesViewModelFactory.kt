package com.vyakhirev.filmsinfo.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.viewmodel.FavoritesViewModel

class FavoritesViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repo) as T
    }
}
