package com.vyakhirev.filmsinfo.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Rect
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
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.view.adapters.FilmsAdapter
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_movie.*

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
            requireContext(),
            listOf(),
            listener = {
                val detMovie = viewModel.movies.value?.get(it)
                viewModel.openDetails(detMovie)
                viewModel.filmIsViewed(detMovie!!.uuid)
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
                dataPicker()
                prefHelper.saveWatchLaterUuid(viewModel.movies.value!![it].uuid)
            })

        filmsRecyclerView.layoutManager = LinearLayoutManager(context)
        filmsRecyclerView.adapter = adapter

        val itemDecor = CustomItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.my_divider)
            ?.let { itemDecor.setDrawable(it) }
        if (filmsRecyclerView.adapter!!.itemCount == 1) {
            filmsRecyclerView.removeItemDecoration(itemDecor)
        }
        filmsRecyclerView.addItemDecoration(itemDecor)
        filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == (viewModel.movies.value?.size )) {
                            viewModel.page++
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
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    var date = """$year-${monthOfYear + 1}-$dayOfMonth"""
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
            ViewModelFactory(App.instance!!.moviesApiClient)
        ).get(FilmListViewModel::class.java)
        viewModel.movies.observe(viewLifecycleOwner, renderMovies)
        viewModel.isViewLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.filmClicked.observe(viewLifecycleOwner, onFilmClicked)
    }

    private val onFilmClicked = Observer<Movie> {
        viewModel.openDetails(it)
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
            viewModel.refresh()
//            viewModel._onMessageError.call()
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

        Log.d(DEBUG_TAG, "onActivityCreated")
    }

    companion object {
        const val TAG = "ListMovieFragment"
        const val DEBUG_TAG = "deb+$TAG"
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
