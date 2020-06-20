package com.vyakhirev.filmsinfo.di

import android.app.Application
import androidx.room.Room
import com.vyakhirev.filmsinfo.data.db.MovieDao
import com.vyakhirev.filmsinfo.data.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: Application) {
    private val moviesDatabase: MoviesDatabase = Room
        .databaseBuilder(mApplication, MoviesDatabase::class.java, "moviesdatabase")
        .build()

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
