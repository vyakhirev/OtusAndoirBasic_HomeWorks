package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.BuildConfig
import com.vyakhirev.filmsinfo.di.AppModule
import com.vyakhirev.filmsinfo.di.CONTEXT_APP
import com.vyakhirev.filmsinfo.di.DaggerViewModelComponent
import com.vyakhirev.filmsinfo.di.TypeOfContext
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.MovieResponse
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilmListViewModel(private val moviesApiClient: MovieApiClient) : ViewModel() {

    init {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(App.instance!!))
            .build()
            .inject(this)
    }

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    val onMessageError: SingleLiveEvent<Any> = SingleLiveEvent()

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    @Inject
    @TypeOfContext(CONTEXT_APP)
    lateinit var prefHelper: SharedPreferencesHelper

    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L

    private val disposable = CompositeDisposable()
    var page = 1

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
        _isViewLoading.value = true
        disposable.add(
            App.instance!!.movieDB.movieDao().getAllMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _movies.value = it
                })
    }

    fun fetchFromRemote() {
        if (movies.value.isNullOrEmpty()) {
            _isViewLoading.value = true
        }

        disposable.add(
            moviesApiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieResponse>() {

                    override fun onSuccess(movieList: MovieResponse) {
                        _isViewLoading.value = false

                        disposable.add(
                            storeLocally(movieList.results)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        )

                        fetchFromDatabase()
                    }

                    override fun onError(e: Throwable) {
                        _isViewLoading.value = false
                        onMessageError.postValue("Internet connection is not available")
                    }
                })
        )
    }

    fun storeLocally(list: List<Movie>): Completable {
        prefHelper.saveUpdateTime(System.nanoTime())
        val dao = App.instance!!.movieDB.movieDao()
        return dao.insertAll(list)
    }

    fun switchFavorite(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .flatMap {
                it.isFavorite = !it.isFavorite
                dao.updateMovie(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                it.message
            }
            .subscribe()
        )
    }

    fun filmIsViewed(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .flatMap {
                it.isViewed = true
                dao.updateMovie(it)
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
