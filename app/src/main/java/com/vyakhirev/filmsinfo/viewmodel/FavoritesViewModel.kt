package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritesViewModel() : ViewModel() {

    private val _favoritesLiveData = MutableLiveData<List<Movie>>()
    val favoritesLiveData: LiveData<List<Movie>> = _favoritesLiveData

    private val _filmClicked = MutableLiveData<Movie>()
    val filmClicked: LiveData<Movie> = _filmClicked

    private val disposable = CompositeDisposable()

    fun loadFavorites() {
        disposable.add(
            App.instance!!.movieDB.movieDao().getFavorites(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _favoritesLiveData.value = it
                })
    }

    fun switchFavorite(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { film ->
                film.isFavorite = !film.isFavorite
                dao.switchFavoriteStar(film)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            })
    }

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }

    fun filmIsViewed(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .flatMap {
                it.isViewed = true
                dao.switchFavoriteStar(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
