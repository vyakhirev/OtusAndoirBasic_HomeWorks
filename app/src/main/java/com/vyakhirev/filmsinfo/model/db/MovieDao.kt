package com.vyakhirev.filmsinfo.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vyakhirev.filmsinfo.model.Movie
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>): Completable

    @Query("SELECT * FROM movie")
    fun getAllMovie(): Flowable<List<Movie>>

    @Query("SELECT * FROM movie WHERE uuid=:Id")
    fun getMovie(Id: Int): Single<Movie>

    @Query("SELECT * FROM movie WHERE isFavorite=:isFavorite")
    fun getFavorites(isFavorite: Boolean): Flowable<List<Movie>>

    @Update
    fun updateMovie(movie: Movie): Single<Int>
}
