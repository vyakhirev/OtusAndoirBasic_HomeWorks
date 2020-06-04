package com.vyakhirev.filmsinfo.model.db
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.vyakhirev.filmsinfo.model.Movie
//
//@Database(entities = arrayOf(Movie::class), version = 1)
//abstract class FavorDatabase : RoomDatabase() {
//    abstract fun favDao(): FavorDao
//
//    companion object {
//        @Volatile
//        private var instance: FavorDatabase? = null
//        private val LOCK = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: buildDatabase(context).also {
//                instance = it
//            }
//        }
//
//        private fun buildDatabase(context: Context) = Room.databaseBuilder(
//            context.applicationContext,
//            FavorDatabase::class.java,
//            "favordatabase"
//        ).build()
//    }
//}