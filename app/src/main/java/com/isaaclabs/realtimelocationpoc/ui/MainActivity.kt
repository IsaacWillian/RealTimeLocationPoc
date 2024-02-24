package com.isaaclabs.realtimelocationpoc.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.isaaclabs.realtimelocationpoc.databinding.ActivityMainBinding
import com.isaaclabs.realtimelocationpoc.utils.LocationUtils
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),OnMapReadyCallback {

    private val mainViewModel: MainViewModel by viewModel()

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mMap: GoogleMap? = null

    val locationUtils : LocationUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.maps.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupObservers()

        requestLocationPermission()
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
                binding.permissionAdvice.visibility = View.GONE
                mMap?.isMyLocationEnabled = true
                locationUtils.startTrackLocation()
            } else {
                binding.permissionAdvice.visibility = View.VISIBLE
            }
        }
    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map
        requestLocationPermission()
    }

    fun setupObservers(){
        mainViewModel.userLocation.observe(this){
            if (it != null){
                binding.address.text = it.address
                mMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), 13f
                    )
                )
            } else {
                binding.address.text = "..."
            }
        }
    }
}


const val PERMISSION_REQUEST_LOCATION = 99
fun AppCompatActivity.requestLocationPermission(){
    val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
    this.requestPermissions(permission, PERMISSION_REQUEST_LOCATION)
}