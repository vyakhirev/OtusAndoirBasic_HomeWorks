package com.vyakhirev.filmsinfo.data

import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName
import com.vyakhirev.filmsinfo.R

// @Entity
// class Movie(
//    posterPath: String,
//    @SerializedName("adult")
//    var isAdult: Boolean,
//    @SerializedName("overview")
//    var overview: String?,
//    @SerializedName("release_date")
//    var releaseDate: String?,
//    genreIds: List<Int>,
//    @SerializedName("id")
//    var id: Int?,
//    @SerializedName("original_title")
//    var originalTitle: String?,
//    @SerializedName("original_language")
//    var originalLanguage: String?,
//    @SerializedName("title")
//    var title: String?,
//    @SerializedName("backdrop_path")
//    var backdropPath: String?,
//    @SerializedName("popularity")
//    var popularity: Double?,
//    @SerializedName("vote_count")
//    var voteCount: Int?,
//    @SerializedName("video")
//    var video: Boolean?,
//    @SerializedName("vote_average")
//    var voteAverage: Double?,
//    var isViewed: Boolean = false,
//    var isFavorite: Boolean = false
// ) {
//    @SerializedName("poster_path")
//    var posterPath: String? = null
//        get() = "https://image.tmdb.org/t/p/w500$field"
// }

@Entity
data class Movie(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int?,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String?,
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String?,
    @ColumnInfo(name = "isViewed")
    var isViewed: Boolean = false,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
) {
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = null
//     get() = "https://image.tmdb.org/t/p/w154$field"
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
var films: ArrayList<Movie> = ArrayList()
var favorites: ArrayList<Movie> = ArrayList()
val indInFavor: MutableList<Int> = mutableListOf()
fun ImageView.loadImage(uri: String?) {
    val options = RequestOptions()
        .error(R.mipmap.ic_launcher_round)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(POSTER_BASE_URL + uri)
        .into(this)
}
