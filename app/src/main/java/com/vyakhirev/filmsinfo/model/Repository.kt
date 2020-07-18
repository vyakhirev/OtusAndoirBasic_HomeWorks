package com.vyakhirev.filmsinfo.model

import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor (private val moviesApiClient: MovieApiClient, private val roomDao: MovieDao) {

    private var refreshTime = java.util.concurrent.TimeUnit.MINUTES.toMillis(5)
    private val prefHelper = App.instance!!.prefHelper
    private val disposable = CompositeDisposable()

    private fun checkCacheDuration() {

        val cachePreference = prefHelper.getCacheDuration()?.toLong()

        try {
            refreshTime = java.util.concurrent.TimeUnit.SECONDS.toMillis(cachePreference!!)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun getAllMovies(page: Int): Single<List<Movie>> {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()

        return if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getFromDb()
        } else {
            prefHelper.saveUpdateTime(System.nanoTime())
            disposable.add(
            moviesApiClient.getPopular(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    roomDao.insertAll(it.results)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                }, { it.message })
            )
            getFromDb()
        }
    }

    private fun getFromDb(): Single<List<Movie>> {
        return roomDao.getAllMovie()
    }

    fun switchFavorite(uuid: Int) {
        roomDao.getMovie(uuid)
            .flatMap {
                it.isFavorite = !it.isFavorite
                roomDao.updateMovie(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                it.message
            }
            .subscribe()
    }

    fun filmIsViewed(uuid: Int) {
        roomDao.getMovie(uuid)
            .flatMap {
                it.isViewed = true
                roomDao.updateMovie(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
