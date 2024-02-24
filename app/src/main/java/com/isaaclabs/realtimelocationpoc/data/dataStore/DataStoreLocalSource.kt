package com.isaaclabs.realtimelocationpoc.data.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.isaaclabs.realtimelocationpoc.data.models.UserLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreLocalSource(private val dataStore:DataStore<Preferences> ) {

    private val LATITUDE = doublePreferencesKey("latitude")
    private val LONGITUDE = doublePreferencesKey("longitude")
    private val ADDRESS = stringPreferencesKey("address")


    suspend fun storeNewLocation(userLocation: UserLocation){
        dataStore.edit { locationDataStore ->
            locationDataStore[LATITUDE] = userLocation.latitude
            locationDataStore[LONGITUDE] = userLocation.longitude
            locationDataStore[ADDRESS] = userLocation.address

        }
    }

    val userLocation: Flow<UserLocation?> = dataStore.data.map{ locationDataStore ->
        val latitude = locationDataStore[LATITUDE]
        val longitude = locationDataStore[LONGITUDE]
        val address = locationDataStore[ADDRESS]

        if(latitude != null && longitude != null || address != null) {
            UserLocation(
                address!!,
                latitude!!,
                longitude!!,
            )
        } else {
            null
        }

    }


}