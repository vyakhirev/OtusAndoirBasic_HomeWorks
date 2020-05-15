package com.vyakhirev.filmsinfo.view

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
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import com.vyakhirev.filmsinfo.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail_movie.*

class DetailMovieFragment : Fragment() {
    private lateinit var viewModel: FilmListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Kan","DetailMovieFragment created(")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            activity!!,
            ViewModelFactory(App.instance!!.repository)
        ).get(FilmListViewModel::class.java)
        viewModel.filmClicked.observe(this, filmDetails)
    }

    private val filmDetails = Observer<Movie> { movie ->
        poster.loadImage(movie.posterPath)
        titleTV.text = movie.title
        descrTV.text = movie.overview
    }

    companion object {
        const val TAG = "DetailMovieFragment"
        private const val FILM_INDEX = "film_index"

        fun newInstance(): DetailMovieFragment {
            return DetailMovieFragment()
        }
    }
}
