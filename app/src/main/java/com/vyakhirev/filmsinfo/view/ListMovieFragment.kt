package com.vyakhirev.filmsinfo.view

import android.content.Context
import android.graphics.Canvas
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
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.MoviesResponse
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.network.MovieApiClient
import kotlinx.android.synthetic.main.fragment_list_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 *
 */

class ListMovieFragment : Fragment() {

//    lateinit var films: List<Movie>

    interface OnFilmClickListener {
        fun onFilmClick(ind: Int)
    }

    private var listener: OnFilmClickListener? = null

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
        val call = MovieApiClient.apiClient.getTopRatedMovies(BuildConfig.TMDB_API_KEY, "ru")
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                films = response.body()!!.results
                filmsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = FilmsAdapter(
                        context,
                        films
                    ) { listener?.onFilmClick(it) }
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnFilmClickListener) {
            listener = activity as OnFilmClickListener
        } else {
            throw Exception("Activity must implement OnNewsClickListener")
        }

        Log.d(TAG, "onActivityCreated")
    }

    companion object {
        const val TAG = "ListMovieFragment"
    }

    class CustomItemDecoration(context: Context, orientation: Int) :
        DividerItemDecoration(context, orientation) {

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
        }

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
