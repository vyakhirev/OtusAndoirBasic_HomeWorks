package com.vyakhirev.filmsinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.BuildConfig
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.MovieResponse
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmListViewModel() : ViewModel(), CoroutineScope {
    companion object {
        const val DEBUG_TAG = "deb"
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

    private val disposable = CompositeDisposable()
    private val moviesService = MovieApiClient

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

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
        _isViewLoading.value = true
        disposable.add(
            App.instance!!.movieDB.movieDao().getAllMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _movies.value = it
                })
    }

    var page = 1
    fun fetchFromRemote() {
        _isViewLoading.value = true
        disposable.add(
            moviesService.apiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieResponse>() {

                    override fun onSuccess(movieList: MovieResponse) {
                        _isViewLoading.value = false
                        _movies.postValue(movieList.results)
                        Log.d(DEBUG_TAG, "fetchFromRemote()")
                        storeLocally(movieList.results)
                    }

                    override fun onError(e: Throwable) {
                        _isViewLoading.value = false
                        _onMessageError.postValue(e.printStackTrace())
                    }
                })
        )
    }

    fun storeLocally(list: List<Movie>) {
        launch {
            withContext(Dispatchers.IO) {
                val dao = App.instance!!.movieDB.movieDao()
                dao.deleteAllMovies()
                val result = dao.insertAll(*list.toTypedArray())
                var i = 0
                while (i < list.size) {
                    list[i].uuid = result[i].toInt()
                    ++i
                }
            }
            prefHelper.saveUpdateTime(System.nanoTime())
        }
    }

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }

    fun switchFavorite(uuid: Int) {
        launch {
            withContext(Dispatchers.IO) {
                val dao = App.instance!!.movieDB.movieDao()
                val film = dao.getMovie(uuid)
                film.isFavorite = !film.isFavorite
                dao.switchFavoriteStar(film)
            }
        }
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
// private fun moviesRetrieved(movies: List<Movie>) {
//     _movies.value = movies
//     _isViewLoading.value = false
//     _onMessageError.value = false
// }

// override fun onCleared() {
//     super.onCleared()
//    disposable.clear()
// }
