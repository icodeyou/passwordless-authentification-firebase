package com.hobeez.passwordlessfirebase

import android.app.Application
import com.hobeez.passwordlessfirebase.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(
                viewModelModules
            )
        }
    }
}
