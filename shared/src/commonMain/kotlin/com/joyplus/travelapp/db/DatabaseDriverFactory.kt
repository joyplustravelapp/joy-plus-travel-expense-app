package com.joyplus.travelapp.db

import app.cash.sqldelight.db.SqlDriver

/**
 * Platform-specific factory interface for creating SQLite database drivers.
 * This will be implemented differently on Android and iOS.
 */
interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}