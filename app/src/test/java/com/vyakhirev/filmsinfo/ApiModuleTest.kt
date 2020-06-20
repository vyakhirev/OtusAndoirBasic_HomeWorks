package com.vyakhirev.filmsinfo

import com.vyakhirev.filmsinfo.di.ApiModule
import com.vyakhirev.filmsinfo.data.network.MovieApiClient

class ApiModuleTest(private val mockClient: MovieApiClient) : ApiModule() {
    override fun apiClient(): MovieApiClient {
        return mockClient
    }
}
