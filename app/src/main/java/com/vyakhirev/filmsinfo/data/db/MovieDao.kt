package com.vyakhirev.filmsinfo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vyakhirev.filmsinfo.data.Movie

@Dao
interface MovieDao {
    @Insert
    fun insertAll(vararg movies: Movie): List<Long>

    @Query("SELECT * FROM movie")
    fun getAllMovie(): List<Movie>

    @Query("SELECT * FROM movie WHERE uuid=:Id")
    fun getMovie(Id: Int): Movie

    @Query("SELECT * FROM movie WHERE isFavorite=:isFavorite")
    // @Query("SELECT * FROM movie WHERE isFavorite=:isFavorite")
    fun getFavorites(isFavorite: Boolean): List<Movie>

    @Query("DELETE FROM movie")
    fun deleteAllMovies()

    @Update
    fun switchFavoriteStar(movie: Movie)

    // @Update("SELECT * FROM movie WHERE isFavorite=:isFavorite")
    // @Update(movie)
    // fun swithcFavorite(Id: Int):Movie
}
