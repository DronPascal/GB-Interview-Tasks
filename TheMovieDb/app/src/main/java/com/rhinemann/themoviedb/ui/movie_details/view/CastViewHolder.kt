package com.rhinemann.themoviedb.ui.movie_details.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rhinemann.themoviedb.R
import com.rhinemann.themoviedb.databinding.ItemCastBinding
import com.rhinemann.themoviedb.domain.models.Cast

/**
 * Created by dronpascal on 14.04.2022.
 */
class CastViewHolder(
    private val binding: ItemCastBinding,
    private val onItemClick: (Cast) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: Cast) = with(binding) {
        tvName.text = cast.name
        tvRole.text = cast.character
        Glide.with(itemView)
            .load(cast.imageUrl)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_cinema)
            .into(ivActor)
        root.setOnClickListener { onItemClick(cast) }
    }

}