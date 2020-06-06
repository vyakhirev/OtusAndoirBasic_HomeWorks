package com.vyakhirev.filmsinfo.util

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vyakhirev.filmsinfo.App

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private val prefHelper = App.instance!!.prefHelper

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
