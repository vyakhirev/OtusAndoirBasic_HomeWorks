package com.vyakhirev.filmsinfo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.view.adapters.FavoritesAdapter
import com.vyakhirev.filmsinfo.viewmodel.FavoritesViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.FavoritesViewModelFactory
import kotlinx.android.synthetic.main.fragment_favorites_list.*

class FavoritesListFragment : Fragment() {
    interface OnFavorClickListener {
        fun onFavorToDetails(ind: Int)
    }

    private var listener: OnFavorClickListener? = null
    private var listenerDel: OnFavorClickListener? = null
    private lateinit var favViewModel: FavoritesViewModel
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(DEBUG_TAG, "FavoritesListFragment created(")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecycler()
    }

    private fun setupViewModel() {
        favViewModel =
            ViewModelProvider(
                requireActivity(),
                FavoritesViewModelFactory()
            ).get(FavoritesViewModel::class.java)
        favViewModel.loadFavorites()
        favViewModel.favoritesLiveData.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
        })
        favViewModel.filmClicked.observe(viewLifecycleOwner, Observer {
            favViewModel.openDetails(it)
        })
    }

    private fun setupRecycler() {
        adapter = FavoritesAdapter(
            requireContext(),
            listOf(),
            listener = {
                val detMovie = favViewModel.favoritesLiveData.value!![it]
                favViewModel.openDetails(detMovie)
                favViewModel.filmIsViewed(detMovie.uuid)
                adapter.notifyItemChanged(it)
                listener?.onFavorToDetails(it)
            },
            listenerDel = {
                favViewModel.switchFavorite(favViewModel.favoritesLiveData.value!![it].uuid)
                favViewModel.favoritesLiveData.value!![it].isFavorite =
                    !favViewModel.favoritesLiveData.value!![it].isFavorite
                adapter.notifyItemRemoved(it)
            }
        )
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoritesRecyclerView.adapter = adapter
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

    companion object {
        const val TAG = "FavoritesListFragment"
        const val DEBUG_TAG = "Deb"
    }
}
