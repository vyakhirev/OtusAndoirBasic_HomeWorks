package com.vyakhirev.filmsinfo.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.data.Movie
import com.vyakhirev.filmsinfo.data.loadImage
import com.vyakhirev.filmsinfo.presentation.viewmodel.FavoritesViewModel
import com.vyakhirev.filmsinfo.presentation.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.presentation.viewmodel.factories.FavoritesViewModelFactory
import com.vyakhirev.filmsinfo.presentation.viewmodel.factories.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail_movie.*

class DetailMovieFragment : Fragment() {
    private lateinit var viewModel: FilmListViewModel
    private lateinit var favViewModel: FavoritesViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(DEBUG_TAG, "DetailMovieFragment created(")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                App.instance!!.moviesApiClient
            )
        ).get(FilmListViewModel::class.java)

        viewModel.filmClicked.observe(viewLifecycleOwner, filmDetails)

        favViewModel =
            ViewModelProvider(requireActivity(),
                FavoritesViewModelFactory()
            ).get(FavoritesViewModel::class.java)

        favViewModel.filmClicked.observe(viewLifecycleOwner, filmDetails)
    }

    private val filmDetails = Observer<Movie> { movie ->
        poster.loadImage(movie.posterPath)
        titleTV.text = movie.title
        descrTV.text = movie.overview
    }

    companion object {
        const val TAG = "DetailMovieFragment"
        const val DEBUG_TAG = "Deb"
    }
}
