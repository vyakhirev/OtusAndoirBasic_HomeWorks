package com.vyakhirev.filmsinfo.di.modules

import android.app.Application
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RoomModule(mApplication: Application) {

    private val moviesDatabase: MoviesDatabase = MoviesDatabase.getDatabase(mApplication)

    @Singleton
    @Provides
    fun providesRoomDatabase(): MoviesDatabase {
        return moviesDatabase
    }

    @Singleton
    @Provides
    fun providesmovieDao(moviesDatabase: MoviesDatabase): MovieDao {
        return moviesDatabase.movieDao()
    }
}
