package com.vyakhirev.filmsinfo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.loadImage
import com.vyakhirev.filmsinfo.viewmodel.FavoritesViewModel
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import kotlinx.android.synthetic.main.fragment_detail_movie.*

class DetailMovieFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poster.loadImage(arguments?.getString("poster"))
        titleTV.text =arguments?.getString("title")
        descrTV.text = arguments?.getString("overview")
    }
}
