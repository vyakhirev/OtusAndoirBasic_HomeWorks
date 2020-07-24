package com.vyakhirev.filmsinfo.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.di.components.DaggerAppComponent
import com.vyakhirev.filmsinfo.di.modules.AppModule
import com.vyakhirev.filmsinfo.di.modules.PrefsModule
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.util.MyWorker
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import com.vyakhirev.filmsinfo.view.adapters.FilmsAdapter
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.ViewModelFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_list_movie.*

class ListMovieFragment : Fragment() {

    private var listener: OnFilmClickListener? = null
    private var listenerMy: OnFilmClickListener? = null
    private lateinit var adapter: FilmsAdapter

    init {
        DaggerAppComponent.builder()
            .prefsModule(PrefsModule(App.instance!!))
            .appModule(AppModule(App.instance!!))
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModel: FilmListViewModel

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var prefHelper: SharedPreferencesHelper

    interface OnFilmClickListener {
        fun onFilmClick(detMovie: Movie) { }
        fun onFavorClick(movie: Movie) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            super.onViewCreated(view, savedInstanceState)
            setupViewModel()
            setupRecyclerView()
            setupRefreshLayout()
        }
    }

    private fun setupRecyclerView() {
        adapter = FilmsAdapter(
            requireContext(),
            listOf(),

            listener = {
                viewModel.filmIsViewed(it.uuid)
                listener?.onFilmClick(it)
            },

            listenerMy = {
                viewModel.switchFavorite(it.uuid)
                viewModel.movies.value!![it.uuid - 1].isFavorite = !viewModel.movies.value!![it.uuid-1].isFavorite
                adapter.notifyItemChanged(it.uuid - 1)
                listenerMy?.onFavorClick(it)
            },

            listenerWl = {
                prefHelper.saveWatchLaterData("no")
                dataPicker()

                prefHelper.apply {
                    saveWatchLaterUuid(it.uuid)
                    saveWatchLaterTitle(it.title)
                    saveWatchLaterPoster(it.posterPath)
                    saveWatchLaterOverview(it.overview)
                }
                scheduleJob()
            })

        filmsRecyclerView.layoutManager = LinearLayoutManager(context)
        filmsRecyclerView.adapter = adapter

        val itemDecor = CustomItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.my_divider)
            ?.let { itemDecor.setDrawable(it) }
        filmsRecyclerView.addItemDecoration(itemDecor)

        filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == (viewModel.movies.value?.size?.minus(3))) {
                    viewModel.page++
                    viewModel.getMovies()
                }
            }
        })
    }

    private fun scheduleJob() {
        val request = PeriodicWorkRequest
            .Builder(
                MyWorker::class.java,
                16, TimeUnit.MINUTES,
                16, TimeUnit.MINUTES
            )
            .build()
        WorkManager
            .getInstance(activity!!.applicationContext)
            .enqueue(request)
    }

    private fun dataPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val date = """$year-${monthOfYear + 1}-$dayOfMonth"""
                    prefHelper.saveWatchLaterData(date)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(repository)
        ).get(FilmListViewModel::class.java)

        viewModel.apply {
            movies.observe(viewLifecycleOwner, renderMovies)
            isViewLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
            onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        }
    }

    private val renderMovies = Observer<List<Movie>> {
        Log.d(DEBUG_TAG, "renderMovies, size=${it.size} ")
        progressBar.visibility = View.GONE
        loadingTV.visibility = View.GONE
        if (it == null) filmsRecyclerView.visibility = View.GONE
        else filmsRecyclerView.visibility = View.VISIBLE
        adapter.update(it)
    }

    @SuppressLint("SetTextI18n")
    private val onMessageErrorObserver = Observer<Any> {
        errorImg.visibility = View.VISIBLE

        errorTV.text = "Error $it"
        errorTV.visibility = View.VISIBLE

        retryBtn.visibility = View.VISIBLE
        filmsRecyclerView.visibility = View.GONE

        retryBtn.setOnClickListener {
            viewModel.getMovies()
            errorImg.visibility = View.GONE
            errorTV.visibility = View.GONE
            retryBtn.visibility = View.GONE
            filmsRecyclerView.visibility = View.VISIBLE
        }
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val visibility = if (it) View.VISIBLE else View.GONE

        progressBar.visibility = visibility
        loadingTV.visibility = visibility

        if (it) {
            filmsRecyclerView.visibility = View.GONE
        } else filmsRecyclerView.visibility = View.VISIBLE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnFilmClickListener) {
            listener = activity as OnFilmClickListener
            listenerMy = activity as OnFilmClickListener
        } else {
            throw Exception("Activity must implement ClickListener")
        }
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            viewModel.getMovies()
            refreshLayout.isRefreshing = false
            filmsRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        const val TAG = "ListMovieFragment"
        const val DEBUG_TAG = "deb+$TAG"
    }
}
