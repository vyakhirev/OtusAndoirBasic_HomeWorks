package com.vyakhirev.filmsinfo

import android.content.Context
import android.opengl.Visibility
import android.util.Log
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
                favorites[currentPosition].isFavorite=false
                notifyItemRemoved(currentPosition)
                Log.d("Kan2", "$currentPosition...removed!")
            }
        }

        fun setData(film: Film, pos: Int) {
            if (favorites[pos].isFavorite) {
                itemView.favTitleTV.text = film.title
                this.currentFilm = film
                this.currentPosition = pos
            } else {
                itemView.visibility = View.GONE
                itemView.layoutParams.height=0
                itemView.layoutParams.width=0
//                RecyclerView.LayoutParams(0, 0)
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
