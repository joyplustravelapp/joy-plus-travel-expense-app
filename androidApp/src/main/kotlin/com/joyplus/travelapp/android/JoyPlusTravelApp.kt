package com.joyplus.travelapp.android

import android.app.Application
import com.joyplus.travelapp.android.di.viewModelModule
import com.joyplus.travelapp.db.AndroidDatabaseDriverFactory
import com.joyplus.travelapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.dsl.module

/**
 * Main Application class for the Android app.
 * Initializes Koin for dependency injection.
 */
class JoyPlusTravelApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin for dependency injection
        initKoin(
            module {
                single { AndroidDatabaseDriverFactory(androidContext()) }
            },
            androidModule = {
                androidLogger(Level.ERROR) // Use ERROR level to avoid Koin Android logger issues
                androidContext(this@JoyPlusTravelApp)
                modules(viewModelModule)
            }
        )
    }
}