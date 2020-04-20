package com.vyakhirev.filmsinfo.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vyakhirev.filmsinfo.R
import kotlinx.android.synthetic.main.fragment_detail_movie.*

// const val FILM_INDEX = "film_index"
const val THEME_SWITCHER = "theme_switcher"
private var filmClicked: Int = 10000
private var themesSwitcher = true

class MainActivity : AppCompatActivity(), ListMovieFragment.OnFilmClickListener,
    FavoritesListFragment.OnFavorClickListener {

    override fun onFilmClick(ind: Int) {
        openFilmDetailed(ind)
    }

    override fun onFavorClick(ind: Int) {
        openFilmDetailed(ind)
    }

    private fun openFilmDetailed(ind: Int) {
        Log.d("Kan", "OpenDetail")
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                DetailMovieFragment.newInstance(ind),
                DetailMovieFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId=R.id.action_list
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.action_list -> {
                    val firstFragment =
                        ListMovieFragment()
                    openFragment(firstFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_favorites -> {
                    val secondFragment =
                        FavoritesListFragment()
                    openFragment(secondFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_settings -> {
                    val thirdFragment =
                        FavoritesListFragment()
                    openFragment(thirdFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            myExitDialog()
        }
    }

    private fun myExitDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val body = dialog.findViewById(R.id.txt_dia) as TextView
        body.text = getString(R.string.exit_dialog)
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
