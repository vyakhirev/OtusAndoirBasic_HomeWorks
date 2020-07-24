package com.vyakhirev.filmsinfo

import android.app.Application
import android.util.Log
import androidx.work.Configuration

class App : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        instance = this
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

    companion object {
        var instance: App? = null
            private set
    }
}
