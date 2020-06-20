package com.vyakhirev.filmsinfo.presentation.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vyakhirev.filmsinfo.data.Movie

class MovieDiffCallback(private val newRows: List<Movie?>, private val oldRows: List<Movie?>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRow = oldRows[oldItemPosition]
        val newRow = newRows[newItemPosition]
        return oldRow?.uuid == newRow?.uuid
    }

    override fun getOldListSize(): Int = oldRows.size

    override fun getNewListSize(): Int = newRows.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRow = oldRows[oldItemPosition]
        val newRow = newRows[newItemPosition]
        return oldRow == newRow
    }
}
