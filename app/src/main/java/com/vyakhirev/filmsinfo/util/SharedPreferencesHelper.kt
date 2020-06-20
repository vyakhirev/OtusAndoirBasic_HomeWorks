package com.vyakhirev.filmsinfo.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper {

    companion object {

        private const val PREF_TIME = "Pref time"
        private const val PREF_DATE = "Pref date"
        private const val PREF_UUID = "Pref uuid"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferencesHelper? = null

        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance
                ?: synchronized(LOCK) {
                instance
                    ?: buildHelper(
                        context
                    ).also {
                    instance = it
                }
            }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    // Date_
    fun saveWatchLaterData(date: String) {
        prefs?.edit(commit = true) { putString(
            PREF_DATE, date) }
    }

    fun getWatchLaterData() = prefs?.getString(PREF_DATE, "")

    // UUID
    fun saveWatchLaterUuid(uuid: Int) {
        prefs?.edit(commit = true) { putInt(
            PREF_UUID, uuid) }
    }

    fun getWatchLaterUuid() = prefs?.getInt(PREF_UUID, 0)

    // For cache duration
    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) { putLong(
            PREF_TIME, time) }
    }

    fun getUpdateTime() = prefs?.getLong(
        PREF_TIME, 0L)

    fun getCacheDuration() = prefs?.getString("pref_cache_duration", "10")
}
