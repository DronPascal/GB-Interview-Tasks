package com.rhinemann.googlemaps.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rhinemann.googlemaps.R
import com.rhinemann.googlemaps.databinding.ActivityMapsBinding
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPrefs: SharedPreferences
    private var marksList = mutableListOf<Mark>()
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences(SHARED_MARKS_LIST, Context.MODE_PRIVATE)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMapFragment()
        initMarksList()
    }

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    private fun initMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initMarksList() {
        readAllMarksFromPreferences()?.also { marksList = it }
    }

    override fun onRestart() {
        super.onRestart()
        initMarksList()
        updateAllMarks()
    }

    private fun updateAllMarks() {
        map.clear()
        initMarks(marksList)
    }

    @SuppressLint("MissingPermission")
    private fun allowMyLocation() {
        map.isMyLocationEnabled = true
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setMapLongClick(map)
        initMarks(marksList)

        checkOrAskLocationPermission() {
            allowMyLocation()
        }
    }

    private fun readAllMarksFromPreferences(): MutableList<Mark>? {
        val marksJson = sharedPrefs.getString(KEY_MARK_LIST, "")
        if (marksJson?.isNotBlank() == true) {
            val arrayMarkType = object : TypeToken<MutableList<Mark>>() {}.type
            return gson.fromJson(marksJson, arrayMarkType)
        }
        return null
    }

    private fun initMarks(marksList: List<Mark>) {
        marksList.forEach { addMarkerOnMap(it) }
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            val mark = Mark(
                lat = latLng.latitude,
                lon = latLng.longitude,
                title = "Marker",
                snippet = snippet
            )
            addMarkerOnMap(mark)
            marksList.add(mark)
        }
    }

    private fun addMarkerOnMap(mark: Mark) {
        val latLng: LatLng = LatLng(mark.lat, mark.lon)
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(mark.title)
                .snippet(mark.snippet)
        )
    }

    override fun onPause() {
        super.onPause()
        saveAllMarksToSharedPreferences()
    }

    private fun saveAllMarksToSharedPreferences() {
        with(sharedPrefs.edit()) {
            val marksJson = gson.toJson(marksList)
            putString(KEY_MARK_LIST, marksJson)
            apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.markers_screen -> {
            val intent = Intent(this, MarkActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    // Check location permission is granted
    private fun checkOrAskLocationPermission(callback: () -> Unit) {
        // Check GPS is enabled
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            buildAlertMessageNoGps(this)
            return
        }
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            callback.invoke()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }
    }

    private fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Your GPS is disabled. Do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                allowMyLocation()
            }
        }
    }

    companion object {
        const val SHARED_MARKS_LIST = "marks_list"
        const val KEY_MARK_LIST = "serializable_mark_list"
        const val PERMISSIONS_REQUEST = 88
    }
}