package com.vyakhirev.filmsinfo.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.loadImage
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoritesAdapter(
    private val context: Context,
    private var favorMovieList: List<Movie>,
    private val listener: ((ind: Int) -> Unit)?,
    private val listenerDel: ((ind: Int) -> Unit)?
) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    fun update(data: List<Movie>) {
        favorMovieList = data
        notifyDataSetChanged()
    }
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
            itemView.favTitleTV.setOnClickListener {
                listener?.invoke(currentPosition)
            }
            itemView.favorPosterIV.setOnClickListener {
                listener?.invoke(currentPosition)
            }
            itemView.deleteIV.setOnClickListener {
                listenerDel?.invoke(currentPosition)
            }
        }

        fun setData(film: Movie, pos: Int) {
            this.currentFilm = film
            this.currentPosition = pos
            itemView.favTitleTV.text = film.title
            if (favorMovieList[pos].isViewed) itemView.favTitleTV.setTextColor(Color.BLUE)
            else itemView.favTitleTV.setTextColor(Color.GRAY)
            itemView.favorPosterIV.loadImage(favorMovieList[pos].posterPath)
        }
    }
}
