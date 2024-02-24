package com.isaaclabs.realtimelocationpoc

import android.app.Application
import com.isaaclabs.realtimelocationpoc.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }



}