package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val _favoritesLiveData = MutableLiveData<List<Movie>>()
    val favoritesLiveData: LiveData<List<Movie>> = _favoritesLiveData

    private val disposable = CompositeDisposable()

    fun loadFavorites() {
        disposable.add(
            repo.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _favoritesLiveData.value = it
                })
    }

    fun filmIsViewed(uuid: Int) {
        repo.filmIsViewed(uuid)
    }

    fun switchFavorite(uuid: Int) {
        repo.switchFavorite(uuid)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
