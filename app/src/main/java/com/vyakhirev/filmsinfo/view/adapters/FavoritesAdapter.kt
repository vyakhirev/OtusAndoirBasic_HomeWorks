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
    val listener: ((movie: Movie) -> Unit)?,
    val listenerDel: ((movie: Movie) -> Unit)?
) :
    RecyclerView.Adapter<FavoritesViewHolder>() {
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
        holder.bind(favorMovieList[position])
        holder.itemView.favTitleTV.setTextColor(if (favorMovieList[position].isViewed)
                Color.BLUE else Color.GRAY)
            holder.itemView.favTitleTV.setOnClickListener {
                listener?.invoke(favorMovieList[position])
            }
        holder.itemView.favorPosterIV.setOnClickListener {
            listener?.invoke(favorMovieList[position])
        }
        holder.itemView.deleteIV.setOnClickListener {
            listenerDel?.invoke(favorMovieList[position])
        }
    }
}
class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) {
            itemView.favTitleTV.text = item.title
            itemView.favorPosterIV.loadImage(item.posterPath)
        }
}
