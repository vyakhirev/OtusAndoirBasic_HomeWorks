package com.vyakhirev.filmsinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
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
import com.vyakhirev.filmsinfo.view.adapters.MovieDiffCallback
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilmListViewModel(private val moviesApiClient: MovieApiClient) : ViewModel() {

    constructor(moviesApiClient: MovieApiClient, test: Boolean = true) : this(moviesApiClient) {
        injected = true
    }

    companion object {
        const val DEBUG_TAG = "deb"
    }

    private var injected = false
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    val onMessageError: SingleLiveEvent<Any> = SingleLiveEvent()

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _filmClicked = MutableLiveData<Movie>()
    val filmClicked: LiveData<Movie> = _filmClicked

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefHelper: SharedPreferencesHelper
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L

    private val disposable = CompositeDisposable()

    fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(App.instance!!))
                .build()
                .inject(this)
        }
    }

    fun refresh() {
        inject()
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
        if (movies.value.isNullOrEmpty()) {
            _isViewLoading.value = true
        }
        disposable.add(
            moviesApiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieResponse>() {

                    override fun onSuccess(movieList: MovieResponse) {
                        _isViewLoading.value = false
                        Log.d(DEBUG_TAG, "fetchFromRemote()")
                        disposable.add(
                            storeLocally(movieList.results)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        )
                        fetchFromDatabase()
                        if (movieList.results.isNullOrEmpty()) {
                            DiffUtil.calculateDiff(
                                MovieDiffCallback(
                                    movies.value!!,
                                    movieList.results
                                )
                            )
                        }
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

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }

    fun switchFavorite(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .flatMap {
                it.isFavorite = !it.isFavorite
                dao.switchFavoriteStar(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        )
    }

    fun filmIsViewed(uuid: Int) {
        val dao = App.instance!!.movieDB.movieDao()
        disposable.add(dao.getMovie(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { film ->
                Log.d("uuu", film.overview)
                film.isViewed =true
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
