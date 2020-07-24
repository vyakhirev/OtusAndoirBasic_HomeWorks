package com.vyakhirev.filmsinfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.di.components.DaggerAppComponent
import com.vyakhirev.filmsinfo.di.modules.AppModule
import com.vyakhirev.filmsinfo.di.modules.PrefsModule
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.view.adapters.FavoritesAdapter
import com.vyakhirev.filmsinfo.viewmodel.FavoritesViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.FavoritesViewModelFactory
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_favorites_list.*

class FavoritesListFragment : Fragment() {
    interface OnFavorClickListener {
        fun onFavorToDetails(detMovie: Movie)
    }

    private var listener: OnFavorClickListener? = null
    private var listenerDel: OnFavorClickListener? = null
    private lateinit var favViewModel: FavoritesViewModel
    private lateinit var adapter: FavoritesAdapter

    init {
        DaggerAppComponent.builder()
            .prefsModule(PrefsModule(App.instance!!))
            .appModule(AppModule(App.instance!!))
            .build()
            .inject(this)
    }

    @Inject
    lateinit var repository: Repository

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
        setupViewModel()
        setupRecycler()
    }

    private fun setupViewModel() {
        favViewModel =
            ViewModelProvider(
                requireActivity(),
                FavoritesViewModelFactory(repository)
            ).get(FavoritesViewModel::class.java)

        favViewModel.getFavorites()

        favViewModel.favoritesLiveData.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
        })
    }

    private fun setupRecycler() {
        adapter = FavoritesAdapter(
            requireContext(),
            listOf(),

            listener = {
                listener?.onFavorToDetails(it)
                favViewModel.filmIsViewed(it.uuid)
            },

            listenerDel = {
                favViewModel.switchFavorite(it.uuid)
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
}
