package com.joyplus.travelapp.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.joyplus.travelapp.db.JoyPlusTravelDatabase

/**
 * Android-specific implementation of the DatabaseDriverFactory.
 */
class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = JoyPlusTravelDatabase.Schema,
            context = context,
            name = "joyplus_travel.db"
        )
    }
}