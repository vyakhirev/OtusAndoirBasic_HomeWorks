package com.vyakhirev.filmsinfo

import com.vyakhirev.filmsinfo.di.modules.ApiModule
import com.vyakhirev.filmsinfo.model.network.MovieApiClient

class ApiModuleTest(private val mockClient: MovieApiClient) : ApiModule() {
    override fun apiClient(): MovieApiClient {
        return mockClient
    }
}
