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
        private const val TITLE = "title"
        private const val POSTER = "poster"
        private const val OVERVIEW = "overview"
        private const val CACHE_DURATION = "pref_cache_duration"
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

    // Title
    fun saveWatchLaterTitle(title: String) {
        prefs?.edit(commit = true) { putString(
            TITLE, title) }
    }

    fun getWatchLaterTitle() = prefs?.getString(TITLE, TITLE)

    // Poster path
    fun saveWatchLaterPoster(poster: String) {
        prefs?.edit(commit = true) { putString(
            POSTER, poster) }
    }

    fun getWatchLaterPoster() = prefs?.getString(POSTER, POSTER)

    // Overview
    fun saveWatchLaterOverview(overview: String) {
        prefs?.edit(commit = true) { putString(
            OVERVIEW, overview) }
    }

    fun getWatchLaterOverview() = prefs?.getString(OVERVIEW, OVERVIEW)

    // For cache duration
    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) { putLong(
            PREF_TIME, time) }
    }

    fun getUpdateTime() = prefs?.getLong(
        PREF_TIME, 0L)

    fun getCacheDuration() = prefs?.getString(CACHE_DURATION, "10")
}
