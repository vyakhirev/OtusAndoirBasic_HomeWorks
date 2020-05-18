package com.vyakhirev.filmsinfo

import android.app.Application
import android.util.Log
import com.vyakhirev.filmsinfo.data.MoviesRepository
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import com.vyakhirev.filmsinfo.data.db.MoviesDatabase
import com.vyakhirev.filmsinfo.network.MovieApiClient
import java.util.concurrent.Executors

class App : Application() {

    private lateinit var moviesApiClient: MovieApiClient
    lateinit var repository: MoviesRepository
    lateinit var movieDB: MoviesDatabase
    lateinit var prefHelper: SharedPreferencesHelper
    override fun onCreate() {
        Log.d("App", "App onCreate")
        super.onCreate()
        instance = this
        initRepos()
        initRoom()
        initSharePref()
    }

    private fun initSharePref() {
        prefHelper = SharedPreferencesHelper.invoke(this)
    }

    private fun initRoom() {
        Executors.newSingleThreadScheduledExecutor().execute {
            //                Db.getInstance(this)?.getPublisherDao()?.getAll()
            movieDB = MoviesDatabase.invoke(this)
        }
    }

    private fun initRepos() {
        moviesApiClient = MovieApiClient
        repository = MoviesRepository(moviesApiClient)
    }

    companion object {
        var instance: App? = null
            private set
    }
}
