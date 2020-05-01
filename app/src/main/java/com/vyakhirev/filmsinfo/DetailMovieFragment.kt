package com.vyakhirev.filmsinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail_movie.*
import kotlin.text.StringBuilder

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
        when (arguments!!.getInt(FILM_INDEX, 0)) {
            0 -> {
                poster.setImageResource(R.drawable.film1)
                val string=StringBuilder(getString(R.string.film1Descr)+getString(R.string.film1Descr))
                string.append(string).append(string).append(string)
                descrTV.text = string
                titleTV.setText(R.string.film1Name)
            }
            1 -> {
                poster.setImageResource(R.drawable.film2)
                val string=StringBuilder(getString(R.string.film2Descr)+getString(R.string.film2Descr))
                string.append(string).append(string).append(string)
                descrTV.text = string
                titleTV.setText(R.string.film2Name)
            }
            2 -> {
                poster.setImageResource(R.drawable.film3)
                val string=StringBuilder(getString(R.string.film3Descr)+getString(R.string.film3Descr))
                string.append(string).append(string).append(string)
                descrTV.text = string

                titleTV.setText(R.string.film3Name)
            }
            3 -> {
                poster.setImageResource(R.drawable.film4)
                val string=StringBuilder(getString(R.string.film4Descr)+getString(R.string.film4Descr))
                string.append(string).append(string).append(string)
                descrTV.text = string
                titleTV.setText(R.string.film4Name)
            }
        }
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
