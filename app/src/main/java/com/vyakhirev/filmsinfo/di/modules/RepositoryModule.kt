package com.vyakhirev.filmsinfo.di.modules

import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class, RoomModule::class, PrefsModule::class])
open class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(apiClient: MovieApiClient, movieDao: MovieDao): Repository = Repository(moviesApiClient = apiClient, roomDao = movieDao)

//    @Provides
//    fun providePrefs(app: Application): SharedPreferencesHelper= SharedPreferencesHelper(app)
}
