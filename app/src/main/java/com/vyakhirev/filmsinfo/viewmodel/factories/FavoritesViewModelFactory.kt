package com.vyakhirev.filmsinfo.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.viewmodel.ViewModelFavorites

class FavoritesViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelFavorites() as T
    }
}
