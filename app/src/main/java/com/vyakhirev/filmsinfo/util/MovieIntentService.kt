package com.vyakhirev.filmsinfo.util

import android.app.IntentService
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.vyakhirev.filmsinfo.App

class MovieIntentService : IntentService(TAG_SCH) {
    private val prefHelper = App.instance!!.prefHelper
    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG_SCH, "Start of onHandleIntent ")
        try {
            Thread.sleep(10000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Log.d(TAG_SCH, "End of onHandleIntent ")
    }

    override fun onCreate() {
        Log.d(TAG_SCH, "onCreate+ ${prefHelper.getWatchLaterData()}")
//        NotificationHelper(App.instance!!.baseContext).createNotification()
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG_SCH, "onStartCommand +${prefHelper.getWatchLaterData()} ")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dayX = "$year-${month + 1}-$day"
        if (prefHelper.getWatchLaterData() == dayX) {
            Log.d("Kan", "DayX=$dayX")
            NotificationHelper(App.instance!!.baseContext).createNotification()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG_SCH, "onDestroy")
        super.onDestroy()
    }
companion object {
    const val TAG_SCH = "MovieSch"
}
    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    init {
        Log.d(TAG_SCH, "<Init>")
    }
}
