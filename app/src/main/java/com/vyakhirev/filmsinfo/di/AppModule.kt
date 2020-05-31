package com.vyakhirev.filmsinfo.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class AppModule(val app: Application) {
    @Singleton
    @Provides
    fun providesApp(): Application = app
}
