package com.rhinemann.googlemaps.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.rhinemann.googlemaps.databinding.ActivityMarkBinding
import com.rhinemann.googlemaps.databinding.FragmentItemBinding

class MyMarkRecyclerViewAdapter(
    private val marks: MutableList<Mark>,
    private val onMarkEditFinished: (Boolean, Int, String) -> Unit
) : RecyclerView.Adapter<MyMarkRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = marks[position]
        println("pos $position")
        holder.titleTextEdit.setText(item.title)
        holder.titleTextEdit.doOnTextChanged { text, _, _, _ ->
            item.title = text.toString()
            onMarkEditFinished(false, position, item.title)
        }
        holder.snippetTextView.text = item.snippet
        holder.deleteButton.setOnClickListener {
            //marks.removeAt(position)
            onMarkEditFinished(true, position, "")
        }
    }

    override fun getItemCount(): Int = marks.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleTextEdit: EditText = binding.markTitle
        val snippetTextView: TextView = binding.markSnippet
        val deleteButton: ImageButton = binding.markDelete
    }

}