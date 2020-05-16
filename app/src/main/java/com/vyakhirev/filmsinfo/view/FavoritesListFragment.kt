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
import com.vyakhirev.filmsinfo.adapters.FavoritesAdapter
import com.vyakhirev.filmsinfo.viewmodel.FavoritesViewModelFactory
import com.vyakhirev.filmsinfo.viewmodel.ViewModelFavorites
import kotlinx.android.synthetic.main.fragment_favorites_list.*

class FavoritesListFragment : Fragment() {
    interface OnFavorClickListener {
        fun onFavorToDetails(ind: Int) {
        }
    }

    private var listener: OnFavorClickListener? = null
    private var listenerDel: OnFavorClickListener? = null
    private lateinit var favViewModel: ViewModelFavorites
    private lateinit var adapter: FavoritesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Kan", "FavoritListFragment created(")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favViewModel =
            ViewModelProvider(this, FavoritesViewModelFactory()).get(ViewModelFavorites::class.java)
        favViewModel.loadFavorites()
        favViewModel.favoritesLiveData.observe(this, Observer {
            adapter.update(it)
        })
        setupRecycler()
    }

    fun setupRecycler() {
        adapter = FavoritesAdapter(
            context!!,
            listOf(),
            listener = {
                val detMovie=favViewModel.favoritesLiveData.value?.get(it)
                favViewModel.openDetails(detMovie)
                detMovie?.isViewed=true
//                viewModel.switchFavorite(detMovie!!.uuid)
                Log.d("Det","Captured movie= $detMovie  It=$it")
                adapter.notifyItemChanged(it)
//                listener?.onFilmClick(it)
                listener?.onFavorToDetails(it)
            },
            listenerDel = {
                favViewModel.switchFavorite(favViewModel.favoritesLiveData.value!![it].uuid)
                Log.d("Fav", "uuid= ${favViewModel.favoritesLiveData.value!![it].uuid.toString()}")
//                favViewModel.favoritesLiveData.value!![it].isFavorite =!favViewModel.favoritesLiveData.value!![it].isFavorite
                adapter.notifyItemRemoved(it)
//                    viewModel.switchFavorite(viewModel.movies.value!![it].uuid)
//                    favViewModel.favoritesLiveData.value[it]
//                    favorites.removeAt(it)
//                    adapter?.notifyDataSetChanged()
//                    films[indInFavor[it]].isFavorite = false
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
    }
}
