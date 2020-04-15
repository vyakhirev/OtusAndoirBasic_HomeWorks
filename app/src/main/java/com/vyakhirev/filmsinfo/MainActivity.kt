package com.vyakhirev.filmsinfo

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        val favor = arrayListOf<Film>()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// RecyclerView
        val layoutManager = LinearLayoutManager(this)
        filmsRecyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = FilmsAdapter(this, Supplier.films)
        filmsRecyclerView.adapter = adapter

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

//    private fun startDetail(ind: Int) {
//        val intent = Intent(this, DetailActivity::class.java)
//        intent.putExtra(FILM_INDEX, ind)
//        when (ind) {
//            0 -> {
//                film1TitleTV.setBackgroundColor(Color.CYAN)
//                filmClicked = 0
//            }
//            1 -> {
//                film2TitleTV.setBackgroundColor(Color.CYAN)
//                filmClicked = 1
//            }
//            2 -> {
//                film3TitleTV.setBackgroundColor(Color.CYAN)
//                filmClicked = 2
//            }
//            3 -> {
//                film4TitleTV.setBackgroundColor(Color.CYAN)
//                filmClicked = 3
//            }
//        }
//        startActivity(intent)
//    }

    //    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
