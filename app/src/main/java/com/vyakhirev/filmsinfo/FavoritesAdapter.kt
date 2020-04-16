package com.vyakhirev.filmsinfo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoritesAdapter(private val context: Context, private val favorites: List<Film>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentFilm: Film? = null
        private var currentPosition = 0

        init {
            itemView.deleteIV.setOnClickListener {
                films[currentPosition].isFavorite = false
                notifyItemRemoved(currentPosition)
            }
        }

        fun setData(film: Film, pos: Int) {
            this.currentFilm = film
            this.currentPosition = pos
            if (favorites[currentPosition].isFavorite) {
                itemView.favTitleTV.text = film.title
                itemView.favorPosterIV.setImageURI(getPosterUri(currentPosition))
            } else {
                itemView.visibility = View.GONE
                itemView.layoutParams.height = 0
                itemView.layoutParams.width = 0
            }
        }

        private fun getPosterUri(ind: Int): Uri {
            return if (ind < 4) {
                Uri.parse("android.resource://com.vyakhirev.filmsinfo/drawable/film" + (ind + 1).toString())
            } else {
                Uri.parse(
                    "android.resource://com.vyakhirev.filmsinfo/drawable/film" + (1..4).random()
                        .toString()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val film = favorites[position]
        holder.setData(film, position)
    }
}
