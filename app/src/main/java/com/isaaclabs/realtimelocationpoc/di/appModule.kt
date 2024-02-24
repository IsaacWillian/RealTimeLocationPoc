package com.isaaclabs.realtimelocationpoc.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.isaaclabs.realtimelocationpoc.data.dataStore.DataStoreLocalSource
import com.isaaclabs.realtimelocationpoc.data.repository.LocationRepository
import com.isaaclabs.realtimelocationpoc.data.repository.LocationRepositoryImpl
import com.isaaclabs.realtimelocationpoc.ui.MainViewModel
import com.isaaclabs.realtimelocationpoc.utils.LocationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { LocationUtils(androidApplication(),get(),get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single { DataStoreLocalSource(androidApplication().dataStore) }

    factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }



    viewModel { MainViewModel(get()) }

}


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="locationDataStore")
