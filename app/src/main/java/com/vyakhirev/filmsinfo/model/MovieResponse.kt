package com.vyakhirev.filmsinfo.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    var page: Int,
    var results: List<Movie>,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    val status: Int?,
    val msg: String = "Error!"
) {
    fun isSuccess(): Boolean = results.isNotEmpty()
}
