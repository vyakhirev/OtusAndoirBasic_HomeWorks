package com.vyakhirev.filmsinfo.util

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.vyakhirev.filmsinfo.util.MovieIntentService.Companion.TAG_SCH

class MovieJobService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        jobFinished(params,true)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG_SCH, "onStartJob: starting job with id: " + params?.jobId)
        val startIntentService = Intent(this, MovieIntentService::class.java)
        ContextCompat.startForegroundService(this,startIntentService)
        return true
    }

}