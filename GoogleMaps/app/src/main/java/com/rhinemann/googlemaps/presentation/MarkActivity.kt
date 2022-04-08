package com.rhinemann.googlemaps.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rhinemann.googlemaps.databinding.ActivityMarkBinding

class MarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkBinding
    private lateinit var sharedPrefs: SharedPreferences
    private var marksList = mutableListOf<Mark>()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences(MapsActivity.SHARED_MARKS_LIST, Context.MODE_PRIVATE)
        binding = ActivityMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readAllMarksFromPreferences()?.also { marksList = it }
        initRecycler()
    }

    private fun readAllMarksFromPreferences(): MutableList<Mark>? {
        val marksJson = sharedPrefs.getString(MapsActivity.KEY_MARK_LIST, "")
        if (marksJson?.isNotBlank() == true) {
            val arrayMarkType = object : TypeToken<MutableList<Mark>>() {}.type
            return gson.fromJson(marksJson, arrayMarkType)
        }
        return null
    }

    private fun initRecycler() {
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyMarkRecyclerViewAdapter(marksList) { isDeleted, index, newTitle ->
                if (isDeleted) {
                    deleteListItem(index)
                } else {
                    marksList[index].title = newTitle
                }
            }
        }
    }

    private fun deleteListItem(index: Int) {
        println("removing elem on pos $index")
        marksList.removeAt(index)
        binding.list.adapter?.apply{
            notifyItemRemoved(index)
            notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        marksList.forEach{println(it.toString())}
        saveAllMarksToSharedPreferences()
    }

    private fun saveAllMarksToSharedPreferences() {
        with(sharedPrefs.edit()) {
            val marksJson = gson.toJson(marksList)
            putString(MapsActivity.KEY_MARK_LIST, marksJson)
            apply()
        }
    }

}