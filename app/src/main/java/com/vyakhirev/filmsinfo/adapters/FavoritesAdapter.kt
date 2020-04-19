package com.vyakhirev.filmsinfo.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.films
import kotlinx.android.synthetic.main.favorite_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class FavoritesAdapter(
    private val context: Context,
    private val favorites: List<Movie>
) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val film = favorites[position]
        holder.setData(film, position)
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentFilm: Movie? = null
        private var currentPosition = 0

        init {
            itemView.deleteIV.setOnClickListener {
                favorites[currentPosition].isFavorite = false
                notifyItemRemoved(currentPosition)
            }
        }

        fun setData(film: Movie, pos: Int) {
            this.currentFilm = film
            this.currentPosition = pos
            if (favorites[currentPosition].isFavorite) {
                itemView.favTitleTV.text = film.title
              itemView.favorPosterIV.loadImage(films[pos].posterPath)
            } else {
                itemView.visibility = View.GONE
                itemView.layoutParams.height = 0
                itemView.layoutParams.width = 0
            }
        }
        private fun ImageView.loadImage(uri: String?) {
            val options = RequestOptions()
                .error(R.mipmap.ic_launcher_round)
            Glide.with(this.context)
                .setDefaultRequestOptions(options)
                .load(uri)
                .into(this)
        }
        private fun openDetails(num: Int) {
            itemView.movieTitleTextView.setTextColor(Color.BLUE)
            favorites[num].isViewed = true
        }
    }
}
