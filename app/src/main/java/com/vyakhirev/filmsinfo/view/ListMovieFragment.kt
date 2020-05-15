package com.vyakhirev.filmsinfo.view

import android.content.Context
import android.graphics.Rect
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
import com.vyakhirev.filmsinfo.adapters.FilmsAdapter
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.favorites
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.data.indInFavor
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_movie.*

class ListMovieFragment : Fragment() {
    private var listener: OnFilmClickListener? = null
    private var listenerMy: OnFilmClickListener? = null
    private lateinit var viewModel: FilmListViewModel
    private lateinit var adapter: FilmsAdapter

    interface OnFilmClickListener {
        fun onFilmClick(ind: Int) {
            films[ind].isViewed = true

        }

        fun onFavorClick(ind: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        Log.d("Kan", "ListFragment created(")
    }

//    fun newInstance():ListMovieFragment {
//        return ListMovieFragment()
//    }
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

    override fun onResume() {
        super.onResume()
        Log.d("Kan","Kanotop")
        viewModel.refresh()
    }

    private fun setupRecyclerView() {
        adapter = FilmsAdapter(
            context!!,
            listOf(),
            listener = {
                viewModel.openDetails(viewModel.movies.value?.get(it))
                listener?.onFilmClick(it)
            },
            listenerMy = {
                viewModel.switchFavorite(viewModel.movies.value!![it].uuid)
                viewModel.movies.value!![it].isFavorite = !viewModel.movies.value!![it].isFavorite
                adapter.notifyItemChanged(it)
                listenerMy?.onFavorClick(it)
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
//                    viewModel.refresh()
                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            activity!!,
            ViewModelFactory(App.instance!!.repository)
        ).get(FilmListViewModel::class.java)
//        if (films.isEmpty()) viewModel.fetchFromRemote()
        if (films.isEmpty()) viewModel.refresh()
        viewModel.movies.observe(this, renderMovies)
        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.filmClicked.observe(this, onFilmClicked)
    }

    private val onFilmClicked = Observer<Movie> {
        viewModel.openDetails(it)
    }

    private val renderMovies = Observer<List<Movie>> {
        progressBar.visibility = View.GONE
        loadingTV.visibility = View.GONE
        adapter.update(it)
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        errorImg.visibility = View.VISIBLE
        errorTV.text = "Error $it"
        errorTV.visibility = View.VISIBLE
        retryBtn.visibility = View.VISIBLE
        filmsRecyclerView.visibility=View.GONE
        retryBtn.setOnClickListener {
            viewModel.refresh()
            errorImg.visibility = View.GONE
            errorTV.visibility = View.GONE
            retryBtn.visibility = View.GONE
            filmsRecyclerView.visibility=View.VISIBLE
        }
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
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

        Log.d(TAG, "onActivityCreated")
    }

    companion object {
        const val TAG = "ListMovieFragment"
        const val itemsInPage = 20
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
            films.clear()
            viewModel.page = 0
            viewModel.fetchFromRemote()
            refreshLayout.isRefreshing = false
            filmsRecyclerView.adapter?.notifyDataSetChanged()
        }
    }
}
