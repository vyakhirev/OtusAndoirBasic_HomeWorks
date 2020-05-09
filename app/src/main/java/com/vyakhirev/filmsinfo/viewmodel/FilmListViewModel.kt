package com.vyakhirev.filmsinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.MovieDataSource
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.network.OperationCallback

class FilmListViewModel(private val repository: MovieDataSource) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _filmClicked = MutableLiveData<Movie>()
    val filmClicked: LiveData<Movie> = _filmClicked

    var page = 1
    fun loadFilms() {
        _isViewLoading.postValue(true)
        repository.retrieveMovies(page++, object : OperationCallback<Movie> {
            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }

            override fun onSuccess(data: List<Movie>?) {
                _isViewLoading.postValue(false)
                if (data != null) {
                    films.addAll(data)
                    _movies.value = films
                }
            }
        })
    }

    fun openDetails(movie: Movie?) {
        _filmClicked.postValue(movie)
    }
}
