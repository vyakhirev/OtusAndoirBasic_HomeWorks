package com.vyakhirev.filmsinfo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.movie_item.view.*

class FilmsAdapter(private val context: Context, private val films: List<Film>) :
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
//    private fun setAnimation(viewToAnimate: View) {
//        if (viewToAnimate.animation == null) {
//            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
//            viewToAnimate.animation = animation
//        }
//    }

//    override fun getItemViewType(position: Int): Int {
//        return if (Supplier.films[position].isFavorite)  VIEW_TYPE_FAVORITE else VIEW_TYPE_ITEM
//    }
//
//    companion object {
//        const val VIEW_TYPE_ITEM= 0
//        const val VIEW_TYPE_FAVORITE = 1
//    }

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
//                    favor.add(films[currentPosition])
                    Toast.makeText(
                        context,
                        itemView.movieTitleTextView.text.toString() + " added to favorites!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else Toast.makeText(
                    context,
                    itemView.movieTitleTextView.text.toString() + " already in favorites!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        private fun openDetails(num: Int) {
            itemView.movieTitleTextView.setBackgroundColor(Color.CYAN)
            films[currentPosition].isViewed = true
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra(FILM_INDEX, num)
            context.startActivity(intent)
        }

        fun setData(film: Film?, pos: Int) {
            if (films[pos].isViewed) itemView.movieTitleTextView.setBackgroundColor(Color.CYAN)
            itemView.movieTitleTextView.text = film!!.title
            itemView.posterImgView.setImageURI(getPosterUri(pos))
            this.currentFilm = film
            this.currentPosition = pos
        }

        fun getPosterUri(ind: Int): Uri {
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
}
