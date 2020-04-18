package com.vyakhirev.filmsinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorites_list.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesListFragment : Fragment() {

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
            adapter = FavoritesAdapter(context, films)
        }
    }
    companion object {
        const val TAG = "FavoritesListFragment"
    }
}
