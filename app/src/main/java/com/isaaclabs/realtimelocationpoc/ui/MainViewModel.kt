package com.isaaclabs.realtimelocationpoc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.isaaclabs.realtimelocationpoc.data.repository.LocationRepository
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn

class MainViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {

    val userLocation = locationRepository.getUserLocation.asLiveData()


}