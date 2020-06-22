package com.vyakhirev.filmsinfo.view

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.coordinatorLayout1

class MainActivity : AppCompatActivity(), ListMovieFragment.OnFilmClickListener,
    FavoritesListFragment.OnFavorClickListener {

    lateinit var viewModel: FilmListViewModel

    private val prefs = App.instance!!.prefHelper

    override fun onFilmClick(ind: Int, detMovie: Movie) {
        super.onFilmClick(ind, detMovie)
        openFilmDetailed(detMovie)
    }

    override fun onFavorClick(ind: Int) {
        showSnack(ind)
        super.onFavorClick(ind)
    }

    override fun onFavorToDetails(ind: Int, detMovie: Movie) {
        openFilmDetailed(detMovie)
    }

    override fun onResume() {
        super.onResume()
        val movieUuid = intent.getIntExtra("uuid", -1)
        if (movieUuid != -1) {
            val movieTitle = prefs.getWatchLaterTitle()
            val moviePoster = prefs.getWatchLaterPoster()
            val movieOverview = prefs.getWatchLaterOverview()
            openFilmDetailed(Movie(1, movieTitle!!, moviePoster!!, movieOverview!!))
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            openFragment(ListMovieFragment())
        }

        setContentView(R.layout.activity_main)
        setupNavigation()
        setupNotification()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(App.instance!!.moviesApiClient)
        ).get(FilmListViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }

    private fun setupNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.action_list -> {
                    supportFragmentManager.popBackStack()
                    openFragment(ListMovieFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_favorites -> {
                    supportFragmentManager.popBackStack()
                    openFragment(FavoritesListFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_settings -> {
                    val thirdFragment =
                        SettingsFragment()
                    openFragment(thirdFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun showSnack(ind: Int) {
        val snack =
            Snackbar.make(coordinatorLayout1, "Films added to favorites", Snackbar.LENGTH_SHORT)

        snack.setAction("Undo") { viewModel.switchFavorite(ind + 1) }

        snack.setActionTextColor(
            ContextCompat.getColor(
                this,
                R.color.indigo
            )
        )

        val layoutParams = snack.view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.apply {
            anchorId = R.id.bottomNav
            anchorGravity = Gravity.TOP
            gravity = Gravity.TOP
        }

        snack.apply {
            view.layoutParams = layoutParams
            view.setBackgroundColor(Color.GRAY)
        }
        snack.show()

        coordinatorLayout1.postDelayed({
            snack.dismiss()
        }, 3000)
    }

    private fun openFilmDetailed(detMovie: Movie) {
        val detFragment = DetailMovieFragment()

        val bundle = Bundle()
        bundle.apply {
            putString("title", detMovie.title)
            putString("poster", detMovie.posterPath)
            putString("overview", detMovie.overview)
        }

        detFragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                detFragment
            )
            .addToBackStack(null)
            .commit()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.tag)
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
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.custom_dialog)
        }

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
