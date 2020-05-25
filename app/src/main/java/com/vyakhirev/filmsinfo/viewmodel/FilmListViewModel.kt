package com.vyakhirev.filmsinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.MovieDataSource
import com.vyakhirev.filmsinfo.network.OperationCallback
import com.vyakhirev.filmsinfo.util.NotificationHelper
import java.util.concurrent.Executors

class FilmListViewModel(private val repository: MovieDataSource) : ViewModel() {
    companion object {
        const val DEBUG_TAG = "Deb"
    }
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _onMessageError = SingleLiveEvent<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _filmClicked = MutableLiveData<Movie>()
    val filmClicked: LiveData<Movie> = _filmClicked

    private val prefHelper = App.instance!!.prefHelper
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
        fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

//    fun clearListMovie(){
//        _movies.postValue(null)
//    }
    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun fetchFromDatabase() {
        Log.d(DEBUG_TAG, "fetchFromDatabase()")
        _isViewLoading.postValue(true)
        Executors.newSingleThreadScheduledExecutor().execute {
            val movie = App.instance!!.movieDB.movieDao().getAllMovie()
            _movies.postValue(movie)
            moviesRetrieved(movie)
        }
    }

    var page = 0
    fun fetchFromRemote() {
        _isViewLoading.postValue(true)
        repository.retrieveMovies(page++, object : OperationCallback<Movie> {
            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
                page--
            }

            override fun onSuccess(data: List<Movie>?) {
                Log.d(DEBUG_TAG, "fetchFromRemote()")
                _isViewLoading.postValue(false)
                if (data != null) {
                    _movies.value=data
                    storeLocally(data)
                }
            }
        })
    }

    fun storeLocally(list: List<Movie>) {
        Executors.newSingleThreadScheduledExecutor().execute {
            val dao = App.instance!!.movieDB.movieDao()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            moviesRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }
    private fun moviesRetrieved(movies: List<Movie>) {
        _movies.value = movies
        _isViewLoading.postValue(false)
    }
    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }

    fun switchFavorite(uuid: Int) {
        Executors.newSingleThreadScheduledExecutor().execute {
            val dao = App.instance!!.movieDB.movieDao()
            val film = dao.getMovie(uuid)
            film.isFavorite = !film.isFavorite
            dao.switchFavoriteStar(film)
        }
    }
}
