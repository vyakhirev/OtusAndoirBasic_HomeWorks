package com.vyakhirev.filmsinfo.model

// class MoviesRepository(private val api: MovieApiClient) : MovieDataSource {
//
//     // private var call: Call<MovieResponse>? = null
//     // private var single: Single<MovieResponse>? = null
//     private val disposable = CompositeDisposable()
//
//     override fun retrieveMovies(page: Int, callback: OperationCallback<Movie>) {
//         disposable.add(
//        api.apiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
//             .subscribeOn(Schedulers.newThread())
//             .observeOn(AndroidSchedulers.mainThread())
//             .subscribeWith(object : DisposableSingleObserver<MovieResponse>() {
//                 override fun onSuccess(movieList: MovieResponse) {
//                     Log.d("Deb)",movieList.results.toString())
//                    callback.onSuccess(movieList.results)
//
//                 }
//                 override fun onError(e: Throwable) {
//                     callback.onError("Error!")
//                 }
//             }
//         )
//         )
//     }
//
//     override fun cancel() {
//
//     }
// }
// single = api.apiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
// single?.enqueue(object : Callback<MovieResponse> {
//     override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
//         callback.onError(t.message)
//     }
//     override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
//         response.body()?.let {
//             if (response.isSuccessful && (it.isSuccess())) {
//                 callback.onSuccess(it.results)
//             } else {
//                 callback.onError(it.msg)
//             }
//         }
//     }
// })
