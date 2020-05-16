package com.vyakhirev.filmsinfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.adapters.FavoritesAdapter
import com.vyakhirev.filmsinfo.data.favorites
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.data.indInFavor
import kotlinx.android.synthetic.main.fragment_favorites_list.*

class FavoritesListFragment : Fragment() {
    interface OnFavorClickListener {
        fun onFavorToDetails(ind: Int) {
            favorites[ind].isViewed = true
        }
    }

    private var listener: OnFavorClickListener? = null
    private var listenerDel: OnFavorClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoritesAdapter(
                context,
                favorites,
                listener = { listener?.onFavorToDetails(it) },
                listenerDel = {
                    favorites.removeAt(it)
                    adapter?.notifyDataSetChanged()
                    films[indInFavor[it]].isFavorite = false

//                    adapter?.notifyItemRangeChanged(
//                        it,
//                        favorites.size-1
//                    )
                }
            )
        }
    }

    companion object {
        const val TAG = "FavoritesListFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is OnFavorClickListener) {
            listener = activity as OnFavorClickListener
            listenerDel = activity as OnFavorClickListener
        } else {
            throw Exception("Activity must implement OnFavorClickedListener")
        }
    }
}
