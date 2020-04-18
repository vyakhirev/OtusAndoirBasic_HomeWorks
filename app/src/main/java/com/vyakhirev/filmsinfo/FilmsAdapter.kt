package com.vyakhirev.filmsinfo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.movie_item.view.*

class FilmsAdapter(private val context: Context, private val films: List<Film>,private val listener: ((ind: Int) -> Unit)?) :
    RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return FilmsViewHolder(view)
    }

    override fun getItemCount(): Int = films.size

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        val film = films[position]
        holder.setData(film, position)

    }

    inner class FilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentFilm: Film? = null
        private var currentPosition = 0

        init {
            itemView.posterImgView.setOnClickListener {
                openDetails(currentPosition)
            }
            itemView.movieTitleTextView.setOnClickListener {
                openDetails(currentPosition)
            }
            itemView.favoritesImgView.setOnClickListener {
                if (!films[currentPosition].isFavorite) {
                    films[currentPosition].isFavorite = true
                    showToast("added to favorites!")
                } else showToast("removed from favorites!")
            }
        }


        fun setData(film: Film?, pos: Int) {
            if (films[pos].isViewed) itemView.movieTitleTextView.setTextColor(Color.BLUE)
            itemView.movieTitleTextView.text = film!!.title
            itemView.posterImgView.setImageURI(getPosterUri(pos))
            this.currentFilm = film
            this.currentPosition = pos
        }

        private fun openDetails(num: Int) {
            itemView.movieTitleTextView.setTextColor(Color.BLUE)
            films[num].isViewed = true
            listener?.invoke(num)
        }

        private fun showToast(msg: String) {
            Toast.makeText(
                context,
                itemView.movieTitleTextView.text.toString() + " " + msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
