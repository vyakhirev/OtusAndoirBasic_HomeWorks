package com.vyakhirev.filmsinfo.di

import com.vyakhirev.filmsinfo.BuildConfig
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.model.network.MovieApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val TIMEOUT = 30L

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttp())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getOkHttp(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(getAuthInterceptor())
        return okHttpClient.build()
    }

    private fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .addQueryParameter("language", "ru")
                .build()
            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    fun provideMovieApiService(): MovieApiInterface {
        return getRetrofit().create(MovieApiInterface::class.java)
    }

    @Provides
    open fun apiClient(): MovieApiClient {
        return MovieApiClient()
    }
}
