package com.vyakhirev.filmsinfo.data

import com.vyakhirev.filmsinfo.data.network.OperationCallback

interface MovieDataSource {

    fun retrieveMovies(page: Int = 1, callback: OperationCallback<Movie>)
    fun cancel()
}
