package com.vyakhirev.filmsinfo.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.loadImage
import kotlinx.android.synthetic.main.movie_item.view.*

class FilmsAdapter(
    private val context: Context,
    private val films: List<Movie>,
    private val listener: ((ind: Int) -> Unit)?
) :
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
        private var currentFilm: Movie? = null
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
                    showSnack()
                } else showToast(films[currentPosition].title + " is already favorites!")
            }
        }

        fun setData(film: Movie?, pos: Int) {
            if (films[pos].isViewed) itemView.movieTitleTextView.setTextColor(Color.BLUE)
            itemView.movieTitleTextView.text = film!!.title
            itemView.posterImgView.loadImage(films[pos].posterPath)
            this.currentFilm = film
            this.currentPosition = pos
        }

        private fun openDetails(num: Int) {
            itemView.movieTitleTextView.setTextColor(Color.BLUE)
            films[num].isViewed = true
            listener?.invoke(num)
        }

        private fun showSnack() {
            val snack =
                Snackbar.make(itemView, "Films added to favorites", Snackbar.LENGTH_INDEFINITE)
            // Создаем кнопку действий
            val listener = View.OnClickListener {
                Log.d("Kan", "Kavtorev")
                films[currentPosition].isFavorite = false
            }
            snack.setAction("Undo", listener)
            snack.setActionTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.indigo
                )
            )
            val snackView = snack.view
            val snackTextId = com.google.android.material.R.id.snackbar_text
            val textView = snackView.findViewById<View>(snackTextId) as TextView
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            snackView.setBackgroundColor(Color.GRAY)
            snack.show()
            itemView.postDelayed({
                snack.dismiss()
            }, 5000)
        }

        private fun showToast(msg: String) {
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
