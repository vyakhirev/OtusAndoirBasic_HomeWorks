package com.vyakhirev.filmsinfo.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val serviceApi: MovieApiClient) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmListViewModel(serviceApi) as T
    }
}
