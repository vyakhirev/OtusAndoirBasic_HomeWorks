package com.vyakhirev.filmsinfo.presentation.viewmodel

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
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.MovieResponse
import com.vyakhirev.filmsinfo.data.Repository
import com.vyakhirev.filmsinfo.data.network.MovieApiClient
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import com.vyakhirev.filmsinfo.presentation.view.adapters.MovieDiffCallback
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

    init {
        inject()
    }

    companion object {
        const val DEBUG_TAG = "deb"
    }

    private var injected = false
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    val onMessageError: SingleLiveEvent<Any> =
        SingleLiveEvent()

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _filmClicked = MutableLiveData<Movie>()
    val filmClicked: LiveData<Movie> = _filmClicked

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefHelper: SharedPreferencesHelper
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L

    private val disposable = CompositeDisposable()

    private lateinit var repository:Repository

    fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(App.instance!!))
                .build()
                .inject(this)
        }
    }






    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
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
