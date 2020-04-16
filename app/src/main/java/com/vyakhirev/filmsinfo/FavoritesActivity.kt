package com.vyakhirev.filmsinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_main.*

var favor = arrayListOf<Film>()
class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        // RecyclerView
        val layoutManager = LinearLayoutManager(this)
        favoritesRecyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = FavoritesAdapter(this, films)
        favoritesRecyclerView.adapter = adapter
    }
}
