package com.vyakhirev.filmsinfo.data.network

import com.vyakhirev.filmsinfo.data.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {

    @GET("movie/top_rated")
    fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int

    ): Call<MovieResponse>
}
