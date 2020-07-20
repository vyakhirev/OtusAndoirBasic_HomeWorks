package com.vyakhirev.filmsinfo.di.components

import com.vyakhirev.filmsinfo.di.modules.ApiModule
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: MovieApiClient)
}
