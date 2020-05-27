package com.vyakhirev.filmsinfo

import com.vyakhirev.filmsinfo.di.ApiModule
import com.vyakhirev.filmsinfo.model.network.MovieApiClient

class ApiModuleTest(private val mockClient: MovieApiClient) : ApiModule() {
    override fun apiClient(): MovieApiClient {
        return mockClient
    }
}
//class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {
//    override fun provideAnimalApiService(): AnimalApiService {
//        return mockService
//    }
//}