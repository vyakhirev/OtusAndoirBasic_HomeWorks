package com.vyakhirev.filmsinfo.view

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
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
import com.vyakhirev.filmsinfo.util.NotificationHelper
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListMovieFragment.OnFilmClickListener,
    FavoritesListFragment.OnFavorClickListener {

    lateinit var viewModel:FilmListViewModel
    private val disposable = CompositeDisposable()

    override fun onFilmClick(ind: Int,detMovie: Movie) {
        super.onFilmClick(ind,detMovie)
        openFilmDetailed(ind,detMovie)
    }

    override fun onFavorClick(ind: Int) {
        showSnack(ind)
        super.onFavorClick(ind)
    }

    override fun onFavorToDetails(ind: Int,detMovie: Movie) {
        openFilmDetailed(ind,detMovie)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//    override fun onResume() {
//        super.onResume()
//        if() {
//            var movieTitle = App.instance!!.prefHelper.getWatchLaterTitle()
//            var moviePoster = App.instance!!.prefHelper.getWatchLaterPoster()
//            var movieOverview = App.instance!!.prefHelper.getWatchLaterOverview()
//            openFilmDetailed(0, Movie(1, movieTitle!!, moviePoster!!, movieOverview!!))
//        }
//    }

    private fun setupNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        openFragment(ListMovieFragment())
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
        snack.setAction("Undo", ({viewModel.switchFavorite(ind + 1)}))
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
        layoutParams.apply {
            anchorId = R.id.bottomNav
            anchorGravity = Gravity.TOP
            gravity = Gravity.TOP

        }

        snack.view.layoutParams = layoutParams
        snack.show()

        coordinatorLayout1.postDelayed({
            snack.dismiss()
        }, 3000)
    }

    private fun openFilmDetailed(ind:Int,detMovie:Movie) {
        val detFragment=DetailMovieFragment()
        val bundle = Bundle()
        bundle.apply {
            putString("title",detMovie.title)
            putString("poster",detMovie.posterPath)
            putString("overview",detMovie.overview)

        }

        detFragment.arguments=bundle
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
