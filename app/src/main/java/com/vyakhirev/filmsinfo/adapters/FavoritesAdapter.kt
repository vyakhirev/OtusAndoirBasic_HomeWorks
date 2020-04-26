package com.vyakhirev.filmsinfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.data.loadImage
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoritesAdapter(
    private val context: Context,
    private val favorMovieList: List<Movie>,
    private val listener: ((ind: Int) -> Unit)?
) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = favorMovieList.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val film = favorMovieList[position]
        holder.setData(film, position)
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentFilm: Movie? = null
        private var currentPosition = 0

        init {
            itemView.setOnClickListener {
                listener?.invoke(currentPosition)
            }
        }

        fun setData(film: Movie, pos: Int) {
            this.currentFilm = film
            this.currentPosition = pos
            itemView.favTitleTV.text = film.title
            itemView.favorPosterIV.loadImage(films[pos].posterPath)
        }
    }
}
