package com.vyakhirev.filmsinfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        when (intent.getIntExtra("number", 0)) {
            0 -> {
                poster.setImageResource(R.drawable.film1)
                descrTV.setText(R.string.film1Descr)
                titleTv.setText(R.string.film1Name)
            }
            1 -> {
                poster.setImageResource(R.drawable.film2)
                descrTV.setText(R.string.film2Descr)
                titleTv.setText(R.string.film2Name)
            }
            2 -> {
                poster.setImageResource(R.drawable.film3)
                descrTV.setText(R.string.film3Descr)
                titleTv.setText(R.string.film3Name)
            }
        }

        shareButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Please watch this movie!")
            intent.putExtra(Intent.EXTRA_TEXT, "This movie is great!")
            intent.type = "text/plain"
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("Films_Info", "Your comments: " + commentsTV.text.toString())
        if (likeCheckBox.isChecked) {
            Log.d("Films_Info", "You like this film!")
        } else {
            Log.d("Films_Info", "You don't like this film!")
        }
    }
}
