package com.vyakhirev.filmsinfo.di.modules

import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class, PrefsModule::class, AppModule::class])
open class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(apiClient: MovieApiClient, movieDao: MovieDao, prefSP: SharedPreferencesHelper): Repository =
        Repository(moviesApiClient = apiClient, roomDao = movieDao, prefHelper = prefSP)
}
