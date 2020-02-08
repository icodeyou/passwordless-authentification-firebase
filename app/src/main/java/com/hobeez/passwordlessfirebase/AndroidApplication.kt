package com.hobeez.passwordlessfirebase

import android.app.Application
import com.hobeez.passwordlessfirebase.di.viewModelModules
import com.hobeez.passwordlessfirebase.util.firebase.AuthUtil
import org.jetbrains.anko.toast
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        AuthUtil.getCurrentUser()?.let { currentUser ->
            AuthUtil.doesUserExistInFirestore(
                currentUser.uid,
                { AuthUtil.signOut()},
                {toast("Welcome back")}
            )
        }

        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(
                viewModelModules
            )
        }
    }
}
