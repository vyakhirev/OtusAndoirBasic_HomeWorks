package com.vyakhirev.filmsinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorites.*

var favor = arrayListOf<Film>()

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoritesAdapter(context, films)
        }
    }
}
