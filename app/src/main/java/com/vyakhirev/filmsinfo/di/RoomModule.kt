package com.vyakhirev.filmsinfo.di

import android.app.Application
import androidx.room.Room
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.db.MoviesDatabase
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

//    @Singleton
//    @Provides
//    fun productRepository(productDao: ProductDao): ProductRepository {
//        return ProductDataSource(productDao)
//    }

}