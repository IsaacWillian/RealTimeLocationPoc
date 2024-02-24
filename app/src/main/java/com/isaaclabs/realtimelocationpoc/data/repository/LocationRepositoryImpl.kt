package com.isaaclabs.realtimelocationpoc.data.repository

import com.isaaclabs.realtimelocationpoc.data.dataStore.DataStoreLocalSource
import com.isaaclabs.realtimelocationpoc.data.models.UserLocation
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val dataStore: DataStoreLocalSource
) : LocationRepository {
    override suspend fun saveUserLocation(userLocation: UserLocation) {
       dataStore.storeNewLocation(userLocation)
    }


    override val getUserLocation: Flow<UserLocation?>
        get() = dataStore.userLocation


}