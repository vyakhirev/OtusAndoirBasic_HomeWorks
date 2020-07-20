package com.vyakhirev.filmsinfo.di.modules

import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    open fun providePrefs(): SharedPreferencesHelper = SharedPreferencesHelper()
}
