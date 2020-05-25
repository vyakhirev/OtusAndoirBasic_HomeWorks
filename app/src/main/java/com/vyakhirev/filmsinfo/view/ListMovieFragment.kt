package com.vyakhirev.filmsinfo.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.LocaleData
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.adapters.FilmsAdapter
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_movie.*
import java.time.LocalDate.parse
import java.util.*
import java.util.logging.Level.parse

class ListMovieFragment : Fragment() {
    private var listener: OnFilmClickListener? = null
    private var listenerMy: OnFilmClickListener? = null
    private lateinit var viewModel: FilmListViewModel
    private lateinit var adapter: FilmsAdapter
    private val prefHelper = App.instance!!.prefHelper

    interface OnFilmClickListener {
        fun onFilmClick(ind: Int) {
        }

        fun onFavorClick(ind: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        Log.d(DEBUG_TAG, "$TAG created(")
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
            context!!,
            listOf(),
            listener = {
                val detMovie = viewModel.movies.value?.get(it)
                viewModel.openDetails(detMovie)
                detMovie?.isViewed = true
                Log.d(DEBUG_TAG, "Captured movie= $detMovie  It=$it")
                adapter.notifyItemChanged(it)
                listener?.onFilmClick(it)
            },
            listenerMy = {
                viewModel.switchFavorite(viewModel.movies.value!![it].uuid)
                viewModel.movies.value!![it].isFavorite = !viewModel.movies.value!![it].isFavorite
                adapter.notifyItemChanged(it)
                listenerMy?.onFavorClick(it)
            },
            listenerWl = {
//                Toast.makeText(context, "Watch later! Number=$it", Toast.LENGTH_SHORT).show()
                dataPicker()
                prefHelper.saveWatchLaterUuid(viewModel.movies.value!![it].uuid)
            })
        filmsRecyclerView.layoutManager = LinearLayoutManager(context)
        filmsRecyclerView.adapter = adapter

        val itemDecor =
            CustomItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        ContextCompat.getDrawable(
            context!!,
            R.drawable.my_divider
        )
            ?.let { itemDecor.setDrawable(it) }
        filmsRecyclerView.addItemDecoration(itemDecor)
        filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == viewModel.movies.value?.size) {
                    viewModel.fetchFromRemote()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG, "ListFragment resume")
        viewModel.refresh()
    }

    private fun dataPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var date = """$year-${monthOfYear + 1}-$dayOfMonth"""
//                    Log.d(DEBUG_TAG, "Date=$date")
                    prefHelper.saveWatchLaterData(date)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
//        return date
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            activity!!,
            ViewModelFactory(App.instance!!.repository)
        ).get(FilmListViewModel::class.java)
        if (films.isEmpty()) viewModel.refresh()
        viewModel.movies.observe(viewLifecycleOwner, renderMovies)
        viewModel.isViewLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.filmClicked.observe(viewLifecycleOwner, onFilmClicked)
    }

    private val onFilmClicked = Observer<Movie> {
        viewModel.openDetails(it)
    }

    private val renderMovies = Observer<List<Movie>> {
        progressBar.visibility = View.GONE
        loadingTV.visibility = View.GONE
        if (it == null) filmsRecyclerView.visibility = View.GONE
        else filmsRecyclerView.visibility = View.VISIBLE
        adapter.update(it)
    }

    @SuppressLint("SetTextI18n")
    private val onMessageErrorObserver = Observer<Any> {
        Log.v(DEBUG_TAG, "onMessageError $it")
        errorImg.visibility = View.VISIBLE
        errorTV.text = "Error $it"
        errorTV.visibility = View.VISIBLE
        retryBtn.visibility = View.VISIBLE
        filmsRecyclerView.visibility = View.GONE
        retryBtn.setOnClickListener {
            viewModel.refresh()
            errorImg.visibility = View.GONE
            errorTV.visibility = View.GONE
            retryBtn.visibility = View.GONE
            filmsRecyclerView.visibility = View.VISIBLE
        }
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        Log.v(DEBUG_TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
        loadingTV.visibility = visibility
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnFilmClickListener) {
            listener = activity as OnFilmClickListener
            listenerMy = activity as OnFilmClickListener
        } else {
            throw Exception("Activity must implement OnNewsClickListener")
        }

        Log.d(DEBUG_TAG, "onActivityCreated")
    }

    companion object {
        const val TAG = "ListMovieFragment"
        const val DEBUG_TAG = "Deb"
    }

    class CustomItemDecoration(context: Context, orientation: Int) :
        DividerItemDecoration(context, orientation) {

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

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            viewModel.page = 0
            viewModel.refresh()
            refreshLayout.isRefreshing = false
            filmsRecyclerView.adapter?.notifyDataSetChanged()
        }
    }
}
