package com.vyakhirev.filmsinfo.data.network

interface OperationCallback<T> {
    fun onSuccess(data: List<T>?)
    fun onError(error: String?)
}
