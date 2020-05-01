package com.vyakhirev.filmsinfo.view

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.data.favorites
import com.vyakhirev.filmsinfo.data.films
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorites_list.*
import kotlinx.android.synthetic.main.fragment_list_movie.*

// const val FILM_INDEX = "film_index"
const val THEME_SWITCHER = "theme_switcher"
private var filmClicked: Int = 10000
private var themesSwitcher = true

class MainActivity : AppCompatActivity(), ListMovieFragment.OnFilmClickListener,
    FavoritesListFragment.OnFavorClickListener {

    override fun onFilmClick(ind: Int) {
        super.onFilmClick(ind)
        openFilmDetailed(ind)
    }

    override fun onFavorClick(ind: Int) {
        super.onFavorClick(ind)
        showSnack(ind)
    }

    override fun onFavorToDetails(ind: Int) {
        super.onFavorToDetails(ind)
        onFilmClick(ind)
    }

    override fun onDeleteFromFavor(ind: Int) {
////        favorites.removeAt(ind)
////        favoritesRecyclerView.adapter?.notifyItemRemoved(ind)
////        favoritesRecyclerView.adapter?.notifyItemRangeChanged(
////            ind,
////            favoritesRecyclerView.adapter!!.itemCount
////        )
    }

    private fun showSnack(ind: Int) {
        val snack =
            Snackbar.make(coordinatorLayout1, "Films added to favorites", Snackbar.LENGTH_SHORT)
        val listener = View.OnClickListener {
            favorites.removeAt(favorites.size - 1)
            films[ind].isFavorite = false
            filmsRecyclerView.adapter?.notifyItemChanged(ind)
        }
        snack.setAction("Undo", listener)
        snack.setActionTextColor(
            ContextCompat.getColor(
                this,
                R.color.indigo
            )
        )
        val snackView = snack.view
        val snackTextId = com.google.android.material.R.id.snackbar_text
        val textView = snackView.findViewById<View>(snackTextId) as TextView
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackView.setBackgroundColor(Color.GRAY)
        val layoutParams = snack.view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.anchorId = R.id.bottomNav
        layoutParams.anchorGravity = Gravity.TOP
        layoutParams.gravity = Gravity.TOP
        snack.view.layoutParams = layoutParams

        snack.show()
        coordinatorLayout1.postDelayed({
            snack.dismiss()
        }, 3000)
    }

    private fun openFilmDetailed(ind: Int) {
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
        openFragment(ListMovieFragment())
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
