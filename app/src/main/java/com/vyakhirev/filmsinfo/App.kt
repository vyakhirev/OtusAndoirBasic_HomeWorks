package com.vyakhirev.filmsinfo

import android.app.Application
import com.vyakhirev.filmsinfo.data.MoviesRepository
import com.vyakhirev.filmsinfo.network.MovieApiClient

class App : Application() {

    private lateinit var moviesApiClient: MovieApiClient
    lateinit var repository: MoviesRepository
    override fun onCreate() {
        super.onCreate()
        instance = this
        initRepos()
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
