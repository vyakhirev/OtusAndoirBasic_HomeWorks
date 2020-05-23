package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelFavorites() : ViewModel(), CoroutineScope {

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
                .subscribe() {
                    _favoritesLiveData.value = it
                })
    }

    fun switchFavorite(uuid: Int) {
        launch {
            withContext(Dispatchers.IO) {
                val dao = App.instance!!.movieDB.movieDao()
                val film = dao.getMovie(uuid)
                film.isFavorite = false
                // film.isFavorite = !film.isFavorite
                dao.switchFavoriteStar(film)
                loadFavorites()
            }
        }
    }

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.clear()
    }
}
