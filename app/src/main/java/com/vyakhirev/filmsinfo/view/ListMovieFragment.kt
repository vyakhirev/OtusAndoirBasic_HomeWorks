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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vyakhirev.filmsinfo.BuildConfig
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.adapters.FilmsAdapter
import com.vyakhirev.filmsinfo.data.MovieResponse
import com.vyakhirev.filmsinfo.data.favorites
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.network.MovieApiClient
import kotlinx.android.synthetic.main.fragment_list_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMovieFragment : Fragment() {

    interface OnFilmClickListener {
        fun onFilmClick(ind: Int) {
            films[ind].isViewed = true
        }

        fun onFavorClick(ind: Int){

        }
    }

    private var listener: OnFilmClickListener? = null
    private var listenerMy: OnFilmClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
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
            loadFilms(1)
            setupRecyclerView()
            setupRefreshLayout()
        }
    }
//    private fun showSnack(view:View,ind: Int) {
//        val snack =
//            Snackbar.make(view, "Films added to favorites", Snackbar.LENGTH_SHORT)
//        val listener = View.OnClickListener {
//            favorites.removeAt(favorites.size - 1)
//            films[ind].isFavorite = false
//            filmsRecyclerView.adapter?.notifyItemChanged(ind)
//        }
//        snack.setAction("Undo", listener)
//        snack.setActionTextColor(
//            ContextCompat.getColor(
//                context!!,
//                R.color.indigo
//            )
//        )
//        val snackView = snack.view
//        val snackTextId = com.google.android.material.R.id.snackbar_text
//        val textView = snackView.findViewById<View>(snackTextId) as TextView
//        textView.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
//        snackView.setBackgroundColor(Color.GRAY)
//        val layoutParams = snack.view.layoutParams as CoordinatorLayout.LayoutParams
//        layoutParams.anchorId = R.id.bottomNav
//        layoutParams.anchorGravity = Gravity.TOP
//        layoutParams.gravity = Gravity.TOP
//        snack.view.layoutParams = layoutParams
//
//        snack.show()
//        coordinatorLayout1.postDelayed({
//            snack.dismiss()
//        }, 3000)
//    }

    private fun setupRecyclerView() {
        filmsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilmsAdapter(
                context,
                films,
                listener = { listener?.onFilmClick(it) },
                listenerMy = {
                    listenerMy?.onFavorClick(it)
                    if (!films[it].isFavorite) {
                        favorites.add(films[it])
                        films[it].isFavorite = true
                    }
//                    showSnack(filmsRecyclerView,it)
                    filmsRecyclerView.adapter?.notifyItemChanged(it)
                })
        }

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
            var pageCount = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == films.size) {
                    pageCount++
                    if (pageCount == 1) {
                        loadFilms(pageCount)
                    } else {
                        loadFilmsMore(pageCount)
                    }
                }
                recyclerView.adapter?.notifyItemRangeInserted(
                    films.size + 1,
                    films.size + itemsInPage
                )
            }
        })
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            films.clear()
            loadFilms(1)
            refreshLayout.isRefreshing = false
            filmsRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    fun loadFilms(page: Int) {
        filmsRecyclerView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        loadingTV.visibility = View.VISIBLE
        val call = MovieApiClient.apiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                films.addAll(response.body()!!.results)
                filmsRecyclerView.adapter?.notifyDataSetChanged()
                filmsRecyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                loadingTV.visibility = View.GONE
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
    }

    fun loadFilmsMore(page: Int) {
        val call = MovieApiClient.apiClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", page)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                films.addAll(response.body()!!.results)
                filmsRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
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
}
