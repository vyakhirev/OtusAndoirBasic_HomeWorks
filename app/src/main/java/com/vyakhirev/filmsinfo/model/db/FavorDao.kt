package com.vyakhirev.filmsinfo.model.db

//import androidx.room.*
//import com.vyakhirev.filmsinfo.model.Movie
//import io.reactivex.Completable
//import io.reactivex.Flowable
//import io.reactivex.Single
//
//@Dao
//interface FavorDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addFavor(movie: Movie): Completable
//
//    @Query("DELETE FROM movie WHERE uuid=:Id")
//    fun deleteFavor(Id:Int): Completable
//
//    @Query("DELETE FROM movie")
//    fun clear(): Completable
//
//    @Query("SELECT * FROM movie ")
//    fun getFavorites(): Flowable<List<Movie>>
//
//}
