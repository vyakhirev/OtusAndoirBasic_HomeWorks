package com.vyakhirev.filmsinfo.model.network

import com.vyakhirev.filmsinfo.di.DaggerApiComponent
import com.vyakhirev.filmsinfo.model.MovieResponse
import io.reactivex.Single
import javax.inject.Inject

class MovieApiClient {
    @Inject
    lateinit var api: MovieApiInterface

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getPopular(apiKey: String, language: String, page: Int): Single<MovieResponse> {
        return api.getPopular(apiKey, language, page)
    }
    // private const val BASE_URL = "https://api.themoviedb.org/3/"
    //
    // val apiClient: MovieApiInterface by lazy {
    //
    //     val retrofit = Retrofit.Builder()
    //         .baseUrl(BASE_URL)
    //         .addConverterFactory(GsonConverterFactory.create())
    //         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    //         .build()
    //
    //     return@lazy retrofit.create(MovieApiInterface::class.java)
    // }
}
