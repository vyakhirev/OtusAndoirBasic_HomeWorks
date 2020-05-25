package com.vyakhirev.filmsinfo.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.viewmodel.WatchLaterViewModel

class WatchLaterViewModelFactory() : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WatchLaterViewModel() as T
    }
}