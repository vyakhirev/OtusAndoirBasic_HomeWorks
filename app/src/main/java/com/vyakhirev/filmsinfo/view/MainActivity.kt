package com.vyakhirev.filmsinfo.view

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
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
import com.vyakhirev.filmsinfo.util.MovieJobService
import com.vyakhirev.filmsinfo.util.NotificationHelper
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ListMovieFragment.OnFilmClickListener,
    FavoritesListFragment.OnFavorClickListener {

    private lateinit var viewModel: FilmListViewModel

    private val disposable = CompositeDisposable()

    companion object {
        const val DEBUG_TAG = "Deb"
        var sJobId = 0
        const val TAG_SCH = "MovieSch"
        const val TAG = "MainActivity"
    }

    override fun onFilmClick(ind: Int) {
        super.onFilmClick(ind)
        openFilmDetailed()
    }

    override fun onFavorClick(ind: Int) {
        super.onFavorClick(ind)
        showSnack(ind)
    }

    override fun onFavorToDetails(ind: Int) {
        openFilmDetailed()
        Log.d(DEBUG_TAG, "fromFavorToDetail")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        setupNotification()
    }

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
//        scheduleJob(this)
    }

    private fun setupNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        openFragment(ListMovieFragment())
    }

    private fun showSnack(ind: Int) {
        val snack =
            Snackbar.make(coordinatorLayout1, "Films added to favorites", Snackbar.LENGTH_SHORT)
        val listener = View.OnClickListener {
            viewModel = ViewModelProvider(
                this, ViewModelFactory(App.instance!!.moviesApiClient)
            ).get(FilmListViewModel::class.java)
            viewModel.switchFavorite(ind + 1)
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

    private fun scheduleJob(context: Context) {
        val jobService = ComponentName(context, MovieJobService::class.java)
        val jobBuilder = JobInfo.Builder(sJobId++, jobService)
        jobBuilder.setMinimumLatency(10000)
        jobBuilder.setOverrideDeadline(15000)
        jobBuilder.setRequiresCharging(false)
        jobBuilder.setBackoffCriteria(
            TimeUnit.SECONDS.toMillis(7200000),
            JobInfo.BACKOFF_POLICY_LINEAR
        )
        Log.i(TAG_SCH, "scheduleJob: adding job to scheduler")
        val jobScheduler =
            context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobBuilder.build())
    }

    override fun onResume() {
        super.onResume()
        val movieUuid = intent.getIntExtra(NotificationHelper.MOVIE_UUID, 0)
        if (movieUuid != 0) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(App.instance!!.moviesApiClient)
            ).get(FilmListViewModel::class.java)
            val dao = App.instance!!.movieDB.movieDao()
            disposable.add(dao.getMovie(movieUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Movie>() {
                    override fun onSuccess(t: Movie) {
                        viewModel.openDetails(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                }
                )
            )
            openFilmDetailed()
        }
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

    private fun openFilmDetailed() {

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                DetailMovieFragment(),
                DetailMovieFragment.TAG
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
