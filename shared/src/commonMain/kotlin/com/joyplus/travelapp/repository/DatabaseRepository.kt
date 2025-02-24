package com.joyplus.travelapp.repository

import com.joyplus.travelapp.db.DatabaseDriverFactory
import com.joyplus.travelapp.db.JoyPlusTravelDatabase

/**
 * Main database repository that provides access to the SQLite database.
 * It initializes the database and creates the tables.
 */
class DatabaseRepository(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = JoyPlusTravelDatabase(databaseDriverFactory.createDriver())
    
    // Expose the SQLDelight generated queries
    val tripQueries = database.tripQueries
    val expenseQueries = database.expenseQueries
}