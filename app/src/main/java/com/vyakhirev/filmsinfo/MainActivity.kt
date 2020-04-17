package com.vyakhirev.filmsinfo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

const val FILM_INDEX = "film_index"
const val THEME_SWITCHER = "theme_switcher"
private var filmClicked: Int = 10000
private var themesSwitcher = true

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (themesSwitcher) {
            themesSwitcher = false
            setTheme(R.style.AppThemeLight)
        } else {
            setTheme(R.style.AppThemeDark)
            themesSwitcher = true
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filmsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilmsAdapter(context, films)
        }
//        ItemDecoration
        val itemDecor = CustomItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.my_divider)?.let { itemDecor.setDrawable(it) }
        filmsRecyclerView.addItemDecoration(itemDecor)

// Themes button handler
        themeBtn.setOnClickListener {
            recreate()
        }
// Favorites btn
        favoritesBtn.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(FILM_INDEX, filmClicked)
        outState.putBoolean("theme_switcher", themesSwitcher)
    }
    // CustomItemDecorator
    class CustomItemDecoration(context: Context, orientation: Int) :
        DividerItemDecoration(context, orientation) {

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = 150
        }
    }

    override fun onBackPressed() {
        showDialog(getString(R.string.exit_dialog))
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val body = dialog.findViewById(R.id.txt_dia) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        yesBtn.setOnClickListener {
            super.onBackPressed()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
