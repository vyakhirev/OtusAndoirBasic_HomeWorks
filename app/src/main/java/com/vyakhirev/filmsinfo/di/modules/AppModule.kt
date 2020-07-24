package com.vyakhirev.filmsinfo.di.modules

import android.content.Context
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {
    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }

    private val moviesDatabase: MoviesDatabase = MoviesDatabase.getDatabase(context)

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
