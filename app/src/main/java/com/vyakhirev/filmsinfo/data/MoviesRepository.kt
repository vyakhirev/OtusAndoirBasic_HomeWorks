package com.vyakhirev.filmsinfo.data

import com.vyakhirev.filmsinfo.BuildConfig
import com.vyakhirev.filmsinfo.network.MovieApiClient
import com.vyakhirev.filmsinfo.network.OperationCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository : MovieDataSource {

    private var call: Call<MovieResponse>? = null

    override fun retrieveMovies(page: Int, callback: OperationCallback<Movie>) {
        call = MovieApiClient.apiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                response.body()?.let {
                    if (response.isSuccessful && (it.isSuccess())) {
                        callback.onSuccess(it.results)
                    } else {
                        callback.onError(it.msg)
                    }
                }
            }
        })
    }

    override fun cancel() {
        call?.cancel()
    }
}
