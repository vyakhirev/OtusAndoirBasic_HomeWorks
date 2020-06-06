package com.vyakhirev.filmsinfo

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.vyakhirev.filmsinfo.model.db.MoviesDatabase
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import java.util.concurrent.Executors

class App : Application(), Configuration.Provider {

    lateinit var moviesApiClient: MovieApiClient
    lateinit var movieDB: MoviesDatabase
    lateinit var prefHelper: SharedPreferencesHelper

    override fun onCreate() {
        Log.d("App", "App onCreate")
        super.onCreate()
        instance = this
        moviesApiClient = MovieApiClient()
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
            movieDB = MoviesDatabase.invoke(this)
//            favorDB = FavorDatabase.invoke(this)
        }
    }

    companion object {
        var instance: App? = null
            private set
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(Log.ERROR)
                .build()
        }
    }
}
