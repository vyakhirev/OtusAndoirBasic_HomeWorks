package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilmListViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val disposable = CompositeDisposable()
    var page = 1

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    val onMessageError: SingleLiveEvent<Any> = SingleLiveEvent()

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    fun getMovies() {
        disposable.add(
        repo.getAllMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _movies.value = it
                _isViewLoading.value = false
            }, {
                _isViewLoading.value = false
                onMessageError.postValue("Internet connection is not available")
            })
        )
    }

    fun filmIsViewed(uuid: Int) {
        repo.filmIsViewed(uuid)
    }

    fun switchFavorite(uuid: Int) {
        repo.switchFavorite(uuid)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
