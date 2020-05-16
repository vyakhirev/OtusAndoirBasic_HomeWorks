package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.data.Movie
import java.util.concurrent.Executors

class ViewModelFavorites() : ViewModel() {

    val favoritesLiveData = MutableLiveData<List<Movie>>()

    private val _filmClicked = MutableLiveData<Movie>()
    val filmClicked: LiveData<Movie> = _filmClicked

    fun loadFavorites() {

        Executors.newSingleThreadScheduledExecutor().execute {
            val movie = App.instance!!.movieDB.movieDao().getFavorites(true)
            favoritesLiveData.postValue(movie)
        }
    }

    fun switchFavorite(uuid: Int) {
        Executors.newSingleThreadScheduledExecutor().execute {
            val dao = App.instance!!.movieDB.movieDao()
            var film = dao.getMovie(uuid)
            film.isFavorite = false
//            film.isFavorite = !film.isFavorite
            dao.switchFavoriteStar(film)
            loadFavorites()
        }
    }

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }
}
