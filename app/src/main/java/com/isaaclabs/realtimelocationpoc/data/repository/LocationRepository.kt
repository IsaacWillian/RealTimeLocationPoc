package com.isaaclabs.realtimelocationpoc.data.repository

import com.isaaclabs.realtimelocationpoc.data.models.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun saveUserLocation(userLocation:UserLocation)

    val getUserLocation: Flow<UserLocation?>

}