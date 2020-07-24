package com.vyakhirev.filmsinfo

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.vyakhirev.filmsinfo.di.components.DaggerAppComponent
import com.vyakhirev.filmsinfo.di.modules.AppModule
import com.vyakhirev.filmsinfo.di.modules.PrefsModule

class App : Application(), Configuration.Provider {
//    lateinit var moviesApiClient: MovieApiClient
//    lateinit var movieDB: MoviesDatabase
//    lateinit var prefHelper: SharedPreferencesHelper
init {
}
    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .prefsModule(PrefsModule(this))
            .appModule(AppModule(this))
            .build()
            .inject(this)
//
        instance = this
//        moviesApiClient = MovieApiClient()
//        initRoom()
//        initSharePref()
//        initFcm()
    }

//    private fun initFcm() {
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("App", "getInstanceId failed", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//
//                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d("App", msg)
//            })
//    }

//    private fun initSharePref() {
//        prefHelper = SharedPreferencesHelper.invoke(this)
//    }

//    private fun initRoom() {
//        movieDB = MoviesDatabase.getDatabase(this)
//    }

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

    companion object {
        var instance: App? = null
            private set
    }
}
