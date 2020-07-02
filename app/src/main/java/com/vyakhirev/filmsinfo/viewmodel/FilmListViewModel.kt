package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.di.AppModule
import com.vyakhirev.filmsinfo.di.DaggerViewModelComponent
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    private val room = App.instance!!.movieDB.movieDao()
    private val repo = Repository(moviesApiClient, room)
    var page = 1

    fun getMovies() {
        repo.getAllMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                _movies.value = it
                _isViewLoading.value = false
            }
            .onErrorReturn {
                _isViewLoading.value = false
                onMessageError.postValue("Internet connection is not available")
            }
            .subscribe()
    }

    fun filmIsViewed(uuid: Int) {
        repo.filmIsViewed(uuid)
    }

    fun switchFavorite(uuid: Int) {
        repo.switchFavorite(uuid)
    }
}
