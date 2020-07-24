package com.vyakhirev.filmsinfo.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.loadImage
import kotlinx.android.synthetic.main.movie_item.view.*

class FilmsAdapter(
    private val context: Context,
    private var films: List<Movie>,
    val listener: ((movie: Movie) -> Unit)?,
    val listenerMy: ((movie: Movie) -> Unit)?,
    val listenerWl: ((movie: Movie) -> Unit)?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_FOOTER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            FilmsViewHolder(
                LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
            )
        } else
            FooterViewHolder(
                LayoutInflater.from(context).inflate(R.layout.footer_progress, parent, false)
            )
    }

    override fun getItemCount(): Int = films.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }

    fun update(data: List<Movie>) {
        val movieDiffUtilCallback = MovieDiffCallback(films, data)
        val diffResult = DiffUtil.calculateDiff(movieDiffUtilCallback)
        films = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder)
            if (position == 0) {
                holder.itemView.visibility = View.GONE
            } else holder.itemView.visibility = View.VISIBLE

        if (holder is FilmsViewHolder) {
            holder.bind(films[position])
            holder.itemView.posterImgView.setOnClickListener {
                listener?.invoke(films[position])
            }
            holder.itemView.movieTitleTextView.setOnClickListener {
                listener?.invoke(films[position])
            }
            holder.itemView.favoritesImgView.setOnClickListener {
                listenerMy?.invoke(films[position])
            }
            holder.itemView.watchLaterImgView.setOnClickListener {
                listenerWl?.invoke(films[position])
            }
        }
    }
}

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class FilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) {
            itemView.movieTitleTextView.text = item.title
            itemView.posterImgView.loadImage(item.posterPath)

            itemView.favoritesImgView.setImageResource(if (item.isFavorite)
                R.drawable.ic_star_on_24dp else R.drawable.ic_star_off_36dp)

            itemView.movieTitleTextView.setTextColor(if (item.isViewed)
                Color.BLUE else Color.GRAY)
        }
    }
