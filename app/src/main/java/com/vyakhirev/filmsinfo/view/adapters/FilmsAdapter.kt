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
    private val listener: ((ind: Int) -> Unit)?,
    private val listenerMy: ((ind: Int) -> Unit)?,
    private val listenerWl: ((ind: Int) -> Unit)?
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
            val film = films[position]
            holder.setData(film, position)
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class FilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentPosition = 0

        init {
            itemView.posterImgView.setOnClickListener {
                listener?.invoke(currentPosition)
            }
            itemView.movieTitleTextView.setOnClickListener {
                listener?.invoke(currentPosition)
            }
            itemView.favoritesImgView.setOnClickListener {
                listenerMy?.invoke(currentPosition)
            }
            itemView.watchLaterImgView.setOnClickListener {
                listenerWl?.invoke(currentPosition)
            }
        }

        fun setData(film: Movie, pos: Int) {
            itemView.movieTitleTextView.text = film.title

            itemView.movieTitleTextView.setTextColor(if (films[pos].isViewed)
                Color.BLUE else Color.GRAY)

            itemView.posterImgView.loadImage(films[pos].posterPath)

            itemView.favoritesImgView.setImageResource(if (films[pos].isFavorite)
                R.drawable.ic_star_on_24dp else R.drawable.ic_star_off_36dp)

            this.currentPosition = pos
        }
    }
}
