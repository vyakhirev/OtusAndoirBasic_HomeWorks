package com.vyakhirev.filmsinfo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

const val FILM_INDEX = "film_index"

private var filmClicked: Int = 0

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        detail1Btn.setOnClickListener { startDetail(0) }
        detail2Btn.setOnClickListener { startDetail(1) }
        detail3Btn.setOnClickListener { startDetail(2) }

        if (savedInstanceState != null) {
            when (savedInstanceState.getInt(FILM_INDEX)) {
                0 -> film1TitleTV.setBackgroundColor(Color.CYAN)
                1 -> film2TitleTV.setBackgroundColor(Color.CYAN)
                2 -> film3TitleTV.setBackgroundColor(Color.CYAN)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("film_index", filmClicked)
    }

    @SuppressLint("ResourceAsColor")
    private fun startDetail(number: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("number", number)
        when (number) {
            0 -> {
                film1TitleTV.setBackgroundColor(Color.CYAN)
                filmClicked = 0
            }
            1 -> {
                film2TitleTV.setBackgroundColor(Color.CYAN)
                filmClicked = 1
            }
            2 -> {
                film3TitleTV.setBackgroundColor(Color.CYAN)
                filmClicked = 2
            }
        }
        startActivity(intent)
    }
}
