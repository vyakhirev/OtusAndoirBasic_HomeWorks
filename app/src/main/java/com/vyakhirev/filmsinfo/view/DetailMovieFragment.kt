package com.vyakhirev.filmsinfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.data.films
import com.vyakhirev.filmsinfo.data.loadImage
import kotlinx.android.synthetic.main.fragment_detail_movie.*

/**
 * A simple [Fragment] subclass.
 */
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
        val index = arguments!!.getInt(FILM_INDEX)
        poster.loadImage(films[index].posterPath)
        titleTV.text = films[index].title
        descrTV.text = films[index].overview
    }
    companion object {
        const val TAG = "DetailMovieFragment"
        private const val FILM_INDEX = "film_index"

        fun newInstance(ind: Int): DetailMovieFragment {
            val fragment = DetailMovieFragment()
            val bundle = Bundle()
            bundle.putInt(FILM_INDEX, ind)
            fragment.arguments = bundle
            return fragment
        }
    }
}
