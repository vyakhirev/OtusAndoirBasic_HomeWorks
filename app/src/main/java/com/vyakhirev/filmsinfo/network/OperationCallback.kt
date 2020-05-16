package com.vyakhirev.filmsinfo.network

interface OperationCallback<T> {
    fun onSuccess(data: List<T>?)
    fun onError(error: String?)
}
