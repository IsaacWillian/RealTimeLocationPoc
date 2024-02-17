package com.isaaclabs.realtimelocationpoc

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.isaaclabs.realtimelocationpoc.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),OnMapReadyCallback {

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mMap: GoogleMap? = null

    var userLocation : Pair<Double,Double>? = null
        set(value) {
            Log.d("TESTE","$value")
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.maps.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.permissionAdvice.setOnClickListener {
            requestLocationPermission()
        }

    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startTrackLocation()
                binding.permissionAdvice.visibility = View.GONE
                mMap?.isMyLocationEnabled = true
            } else {
                binding.permissionAdvice.visibility = View.VISIBLE
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = Pair(location.latitude,location.longitude)

            mMap?.let{
                val latLng = LatLng(location.latitude,location.longitude)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13f))
                val address = Geocoder(this@MainActivity).getFromLocation(location.latitude,location.longitude,1)
                address?.get(0)?.let{
                    binding.address.text = it.getAddressLine(0)
                }
            }

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {
            // Handle provider disabled
        }
    }

    @SuppressLint("MissingPermission")
    fun startTrackLocation(){
        // Create a LocationManager instance
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, locationListener)


    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        requestLocationPermission()
    }
}


const val PERMISSION_REQUEST_LOCATION = 99
fun AppCompatActivity.requestLocationPermission(){
    val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
    this.requestPermissions(permission, PERMISSION_REQUEST_LOCATION)
}