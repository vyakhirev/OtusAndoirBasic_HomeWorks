package com.vyakhirev.filmsinfo.model

import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName
import com.vyakhirev.filmsinfo.R


@Entity
data class Movie(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String,
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String,
    @ColumnInfo(name = "isViewed")
    var isViewed: Boolean = false,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
) {
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = null
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

@GlideModule
class CustomGlideModule : AppGlideModule()

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
fun ImageView.loadImage(uri: String?) {
    val options = RequestOptions()
        .error(R.mipmap.ic_launcher_round)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(POSTER_BASE_URL + uri)
        .into(this)
}
