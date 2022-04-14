package com.rhinemann.themoviedb.ui.movie_details.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rhinemann.themoviedb.databinding.ItemCastBinding
import com.rhinemann.themoviedb.domain.models.Cast

/**
 * Created by dronpascal on 14.04.2022.
 */
class CastAdapter(
    private val onItemClick: (Cast) -> Unit,
) : ListAdapter<Cast, CastViewHolder>(CAST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCastBinding.inflate(layoutInflater, parent, false)

        return CastViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val CAST_COMPARATOR = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean =
                (oldItem.id == newItem.id)

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean =
                (oldItem == newItem)
        }
    }
}