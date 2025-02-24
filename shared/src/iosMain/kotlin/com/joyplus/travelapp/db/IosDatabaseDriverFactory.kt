package com.joyplus.travelapp.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.joyplus.travelapp.db.JoyPlusTravelDatabase

/**
 * iOS-specific implementation of the DatabaseDriverFactory.
 */
class IosDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = JoyPlusTravelDatabase.Schema,
            name = "joyplus_travel.db"
        )
    }
}