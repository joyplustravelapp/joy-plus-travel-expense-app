package com.joyplus.travelapp.di

import com.joyplus.travelapp.db.DatabaseDriverFactory
import com.joyplus.travelapp.repository.DatabaseRepository
import com.joyplus.travelapp.repository.ExpenseRepository
import com.joyplus.travelapp.repository.TripRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Initializes Koin dependency injection framework with the provided platform-specific module.
 */
fun initKoin(platformModule: Module) {
    startKoin {
        modules(
            platformModule,
            repositoryModule
        )
    }
}

/**
 * Koin module for repository classes.
 */
private val repositoryModule = module {
    single { DatabaseRepository(get()) }
    single { TripRepository(get()) }
    single { ExpenseRepository(get()) }
}