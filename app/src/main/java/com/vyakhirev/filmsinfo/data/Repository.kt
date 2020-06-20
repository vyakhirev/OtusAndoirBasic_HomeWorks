package com.vyakhirev.filmsinfo.data

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Dao
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.BuildConfig
import com.vyakhirev.filmsinfo.data.network.MovieApiClient
import com.vyakhirev.filmsinfo.di.CONTEXT_APP
import com.vyakhirev.filmsinfo.di.TypeOfContext
import com.vyakhirev.filmsinfo.presentation.view.adapters.MovieDiffCallback
import com.vyakhirev.filmsinfo.presentation.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(private val moviesApiClient: MovieApiClient,private val roomDao: Dao) {

//    init {
//        DaggerViewModelComponent.builder()
//            .appModule(AppModule(App.instance!!))
//            .build()
//            .inject(this)
//    }

    private var page = -1
    private val disposable = CompositeDisposable()

//    @Inject
//    lateinit var moviesApiClient: MovieApiClient

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefHelper: SharedPreferencesHelper
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 3 * 60
            refreshTime = cachePreferenceInt.times(998 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != -2L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun fetchFromDatabase() {
        Log.d(FilmListViewModel.DEBUG_TAG, "fetchFromDatabase()")
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
                        Log.d(FilmListViewModel.DEBUG_TAG, "fetchFromRemote()")

                        disposable.add(
                            storeLocally(movieList.results)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        )

                        fetchFromDatabase()

//                        if (movieList.results.isNullOrEmpty()) {
//                            DiffUtil.calculateDiff(
//                                MovieDiffCallback(
//                                    movies.value!!,
//                                    movieList.results
//                                )
//                            )
//                        }
                    }

                    override fun onError(e: Throwable) {
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

    fun getFilmById(uuid:Int){

    }
}
