package com.vyakhirev.filmsinfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.viewmodel.WatchLaterViewModel
import com.vyakhirev.filmsinfo.viewmodel.factories.WatchLaterViewModelFactory

class WatchLaterFragment : Fragment() {

    companion object {
        fun newInstance() = WatchLaterFragment()
    }

    private lateinit var watchLaterViewModel: WatchLaterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.watch_later_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
       watchLaterViewModel  = ViewModelProvider(
            activity!!,
            WatchLaterViewModelFactory()
        ).get(WatchLaterViewModel::class.java)
    }

}