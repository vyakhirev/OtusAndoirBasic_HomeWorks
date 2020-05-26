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
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FilmListViewModel() : ViewModel() {
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
                        disposable.add(
                            storeLocally(movieList.results)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        )
                    }

                    override fun onError(e: Throwable) {
                        _isViewLoading.value = false
                        _onMessageError.postValue(e.printStackTrace())
                    }
                })
        )
    }

    fun storeLocally(list: List<Movie>): Completable {
        prefHelper.saveUpdateTime(System.nanoTime())
        val dao = App.instance!!.movieDB.movieDao()
        clearDb()
        return dao.insertAll(list)
    }

    private fun clearDb(): Completable {
        val dao = App.instance!!.movieDB.movieDao()
        return dao.deleteAllMovies()
    }

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }

    fun switchFavorite(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { film->
                Log.d("uuu",film.overview)
                film.isFavorite=!film.isFavorite
               dao.switchFavoriteStar(film)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe()
            })
    }


    override fun onCleared() {
        super.onCleared()
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
