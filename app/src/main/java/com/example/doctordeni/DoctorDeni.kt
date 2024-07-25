package com.example.doctordeni

import android.app.Application
import com.example.doctordeni.di.utilsModule
import com.example.doctordeni.di.viewmodels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DoctorDeni : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(
                listOf(
                    utilsModule,
                    viewmodels
                )
            )
        }
    }
}