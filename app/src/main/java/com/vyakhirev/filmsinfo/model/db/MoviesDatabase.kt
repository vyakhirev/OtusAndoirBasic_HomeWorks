package com.vyakhirev.filmsinfo.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vyakhirev.filmsinfo.model.Movie

@Database(entities = arrayOf(Movie::class), version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

companion object {
    @Volatile
    private var INSTANCE: MoviesDatabase? = null

    fun getDatabase(context: Context): MoviesDatabase {
        val tempInstance = INSTANCE
        if (tempInstance != null) {
            return tempInstance
        }
        val instance = Room.databaseBuilder(
            context.applicationContext,
            MoviesDatabase::class.java,
            "moviesdatabase"
        ).build()
        INSTANCE = instance
        return instance
        }
    }
}
