package com.vyakhirev.filmsinfo

import android.app.Application
import com.vyakhirev.filmsinfo.di.PrefsModule
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}