package com.vyakhirev.filmsinfo

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.vyakhirev.filmsinfo.data.MoviesRepository
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import com.vyakhirev.filmsinfo.data.db.MoviesDatabase
import com.vyakhirev.filmsinfo.data.network.MovieApiClient
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
        initFcm()
    }

    private fun initFcm() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("App", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("App", msg)
            })
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
