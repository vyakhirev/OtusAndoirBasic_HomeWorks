package com.vyakhirev.filmsinfo.model.network

import com.vyakhirev.filmsinfo.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {

    @GET("movie/top_rated")
    fun getPopular(
//        @Query("api_key") apiKey: String,
//        @Query("language") language: String,
        @Query("page") page: Int

    ): Single<MovieResponse>
}
