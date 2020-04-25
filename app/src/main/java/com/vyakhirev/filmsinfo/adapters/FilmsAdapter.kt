package com.vyakhirev.filmsinfo.adapters

import android.content.Context
import android.graphics.Color
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
import com.vyakhirev.filmsinfo.data.favorites
import com.vyakhirev.filmsinfo.data.loadImage
import kotlinx.android.synthetic.main.movie_item.view.*

class FilmsAdapter(
    private val context: Context,
    private val films: List<Movie>,
    private val listener: ((ind: Int) -> Unit)?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun getItemCount(): Int = films.size+1

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount-1) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilmsViewHolder) {
            val film = films[position]
            holder.setData(film, position)
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_FOOTER = 1
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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
                favorites.add(films[currentPosition])
                showSnack()
            }
        }

        fun setData(film: Movie?, pos: Int) {

            itemView.movieTitleTextView.text = film!!.title
            if (films[pos].isViewed) itemView.movieTitleTextView.setTextColor(Color.BLUE)
            else itemView.movieTitleTextView.setTextColor(Color.GRAY)
            itemView.posterImgView.loadImage(films[pos].posterPath)
            this.currentFilm = film
            this.currentPosition = pos
        }

        private fun openDetails(num: Int) {
            films[num].isViewed = true
            listener?.invoke(num)
        }

        private fun showSnack() {
            val snack =
                Snackbar.make(itemView, "Films added to favorites", Snackbar.LENGTH_INDEFINITE)
            val listener = View.OnClickListener {
                favorites.removeAt(favorites.size - 1)
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
            }, 3000)
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
