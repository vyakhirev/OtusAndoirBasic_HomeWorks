package com.vyakhirev.filmsinfo.util

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.di.components.DaggerAppComponent
import com.vyakhirev.filmsinfo.di.modules.AppModule
import com.vyakhirev.filmsinfo.di.modules.PrefsModule
import javax.inject.Inject

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    init {
        DaggerAppComponent.builder()
            .prefsModule(PrefsModule(appContext))
            .appModule(AppModule(appContext))
            .build()
            .inject(this)
    }

    @Inject
    lateinit var prefHelper: SharedPreferencesHelper

    @RequiresApi(Build.VERSION_CODES.N)
    override fun doWork(): Result {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dayX = "$year-${month + 1}-$day"
        return if (prefHelper.getWatchLaterData() == dayX) {
            NotificationHelper(App.instance!!.baseContext).createNotification()
            WorkManager
                .getInstance(applicationContext)
                .cancelAllWork()
            Result.success()
        } else Result.retry()
    }
}
