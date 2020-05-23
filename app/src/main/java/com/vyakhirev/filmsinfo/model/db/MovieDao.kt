package com.vyakhirev.filmsinfo.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vyakhirev.filmsinfo.model.Movie
import io.reactivex.Observable

@Dao
interface MovieDao {
    @Insert
    // suspend fun insertAll(vararg movies: Movie): List<Long>
    fun insertAll(vararg movies: Movie): List<Long>

    @Query("SELECT * FROM movie")
    fun getAllMovie(): Observable<List<Movie>>

    @Query("SELECT * FROM movie WHERE uuid=:Id")
    // suspend fun getMovie(Id: Int): Movie
    fun getMovie(Id: Int): Movie

    @Query("SELECT * FROM movie WHERE isFavorite=:isFavorite")
    // suspend fun getFavorites(isFavorite: Boolean): Observable<List<Movie>>
    fun getFavorites(isFavorite: Boolean): Observable<List<Movie>>

    @Query("DELETE FROM movie")
    // suspend fun deleteAllMovies()
    fun deleteAllMovies()

    @Update
    // suspend fun switchFavoriteStar(movie: Movie)
    fun switchFavoriteStar(movie: Movie)
}
