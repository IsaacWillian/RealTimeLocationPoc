package com.isaaclabs.realtimelocationpoc.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.isaaclabs.realtimelocationpoc.data.models.UserLocation
import com.isaaclabs.realtimelocationpoc.data.repository.LocationRepository
import com.isaaclabs.realtimelocationpoc.data.repository.LocationRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LocationUtils(val context: Context, private val repository: LocationRepository, private val coroutineScope: CoroutineScope) {


    @SuppressLint("MissingPermission")
    fun startTrackLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            100,
            0f,
            locationListener
        )


    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val address =
                Geocoder(context).getFromLocation(location.latitude, location.longitude, 1)?.get(0)
                    ?.getAddressLine(0)
            address?.let {
                val userLocation = UserLocation(it, location.latitude, location.longitude)
                coroutineScope.launch {
                    repository.saveUserLocation(userLocation)
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

}