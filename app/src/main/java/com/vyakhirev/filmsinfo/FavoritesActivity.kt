package com.vyakhirev.filmsinfo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_main.*

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        // RecyclerView
        val layoutManager = LinearLayoutManager(this)
        favoritesRecyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val favor = arrayListOf<Film>()
        var ind:Int=Supplier.films.size-1
        for (i in 0..ind) {
            if (Supplier.films[i].isFavorite)
                favor.add(Supplier.films[i])
        }
        val adapter = FilmsAdapter(this, favor)
        favoritesRecyclerView.adapter = adapter
    }
}
