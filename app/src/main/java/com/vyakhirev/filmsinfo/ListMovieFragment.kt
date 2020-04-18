package com.vyakhirev.filmsinfo

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
import kotlinx.android.synthetic.main.fragment_list_movie.*

/**
 * A simple [Fragment] subclass.
 */
class ListMovieFragment : Fragment() {

    interface OnFilmClickListener {
        fun onFilmClick(ind: Int)
    }

    private var listener: OnFilmClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_movie, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//             Themes button handler
//        themeBtn.setOnClickListener {
//            recreate()
//        }
        favoritesBtn.setOnClickListener {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, FavoritesListFragment(), FavoritesListFragment.TAG)
                .addToBackStack(null)
                .commit()
        }

        filmsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilmsAdapter(context, films) { listener?.onFilmClick(it) }
        }
        val itemDecor = CustomItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(context!!, R.drawable.my_divider)
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
