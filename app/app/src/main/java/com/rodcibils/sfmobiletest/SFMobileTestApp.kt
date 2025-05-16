package com.rodcibils.sfmobiletest

import android.app.Application
import com.rodcibils.sfmobiletest.di.networkModule
import com.rodcibils.sfmobiletest.di.repositoryModule
import com.rodcibils.sfmobiletest.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SFMobileTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SFMobileTestApp)
            modules(viewModelModule, repositoryModule, networkModule)
        }
    }
}
