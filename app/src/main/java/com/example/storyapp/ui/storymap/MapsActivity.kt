package com.example.storyapp.ui.storymap

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.helper.PrefViewModel
import com.example.storyapp.helper.PrefViewModelFactory
import com.example.storyapp.helper.SettingPreferences
import com.example.storyapp.ui.authentication.dataStore
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref)).get(PrefViewModel::class.java)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        prefViewModel.getToken().observe(this){
            if (it.isNotEmpty()){
                viewModel.getListMap(it)
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.listStoryMap.observe(this){
            Log.e("Story Map", "Size : ${it.size}")
            setMarker(it)
        }
    }

    private fun setMarker(it: List<ListStoryItem>) {
        for (story in it) {
            val latlng = LatLng(story.lat, story.lon)
            gMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(story.name)
            )
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isIndoorLevelPickerEnabled = true
        gMap.uiSettings.isCompassEnabled = true
        gMap.uiSettings.isMapToolbarEnabled = true
        getMyLoc()
    }

    private fun getMyLoc() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gMap.isMyLocationEnabled = true
        } else {
            reqPermissionLoc.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private val reqPermissionLoc =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) { getMyLoc() }
        }

}