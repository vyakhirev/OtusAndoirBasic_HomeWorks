package com.vyakhirev.filmsinfo.di

import com.vyakhirev.filmsinfo.data.network.MovieApiClient
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: MovieApiClient)
}
