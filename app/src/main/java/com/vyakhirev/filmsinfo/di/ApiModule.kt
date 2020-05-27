package com.vyakhirev.filmsinfo.di

import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.model.network.MovieApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {
     private val BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    fun provideMovieApiService(): MovieApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieApiInterface::class.java)
    }

    @Provides
    open fun apiClient(): MovieApiClient {
        return MovieApiClient()
    }
}