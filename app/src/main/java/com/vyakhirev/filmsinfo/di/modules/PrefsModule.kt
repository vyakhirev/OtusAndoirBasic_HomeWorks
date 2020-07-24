package com.vyakhirev.filmsinfo.di.modules

import android.content.Context
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class PrefsModule(val context: Context) {

    @Provides
    @Singleton
    open fun providePrefs(): SharedPreferencesHelper = SharedPreferencesHelper.invoke(context)
}
