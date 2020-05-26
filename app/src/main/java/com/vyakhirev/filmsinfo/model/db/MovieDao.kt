package com.vyakhirev.filmsinfo.model.db

import androidx.room.*
import com.vyakhirev.filmsinfo.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>): Completable

    @Query("SELECT * FROM movie")
    fun getAllMovie(): Observable<List<Movie>>

    @Query("SELECT * FROM movie WHERE uuid=:Id")
    fun getMovie(Id: Int): Movie

    @Query("SELECT * FROM movie WHERE isFavorite=:isFavorite")
    fun getFavorites(isFavorite: Boolean): Observable<List<Movie>>

    @Query("DELETE FROM movie")
    fun deleteAllMovies()

    @Update
    fun switchFavoriteStar(movie: Movie)
}
