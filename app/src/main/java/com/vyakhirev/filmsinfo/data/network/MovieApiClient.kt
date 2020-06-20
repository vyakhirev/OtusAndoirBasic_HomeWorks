package com.vyakhirev.filmsinfo.data.network

import com.vyakhirev.filmsinfo.di.DaggerApiComponent
import com.vyakhirev.filmsinfo.data.MovieResponse
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
}
