package com.joyplus.travelapp.repository

import com.joyplus.travelapp.db.Trip as DbTrip
import com.joyplus.travelapp.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for managing Trip data in the database.
 */
class TripRepository(private val databaseRepository: DatabaseRepository) {
    
    /**
     * Creates a new trip in the database.
     */
    suspend fun createTrip(trip: Trip): Long = withContext(Dispatchers.Default) {
        databaseRepository.tripQueries.transactionWithResult {
            databaseRepository.tripQueries.insertTrip(
                name = trip.name,
                destination = trip.destination,
                startDate = trip.startDate.toString(),
                endDate = trip.endDate.toString(),
                budget = trip.budget,
                budgetCurrency = trip.budgetCurrency,
                notes = trip.notes
            )
            
            // Return the last inserted ID
            return@transactionWithResult databaseRepository.tripQueries.selectAll().executeAsList().last().id
        }
    }
    
    /**
     * Updates an existing trip in the database.
     */
    suspend fun updateTrip(trip: Trip) = withContext(Dispatchers.Default) {
        databaseRepository.tripQueries.updateTrip(
            name = trip.name,
            destination = trip.destination,
            startDate = trip.startDate.toString(),
            endDate = trip.endDate.toString(),
            budget = trip.budget,
            budgetCurrency = trip.budgetCurrency,
            notes = trip.notes,
            id = trip.id
        )
    }
    
    /**
     * Deletes a trip from the database.
     */
    suspend fun deleteTrip(id: Long) = withContext(Dispatchers.Default) {
        databaseRepository.tripQueries.deleteTrip(id)
    }
    
    /**
     * Gets all trips as a Flow, which will emit new values when the database changes.
     */
    fun getAllTrips(): Flow<List<Trip>> {
        return databaseRepository.tripQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { dbTrips -> dbTrips.map { it.toTrip() } }
    }
    
    /**
     * Gets a trip by ID.
     */
    suspend fun getTripById(id: Long): Trip? = withContext(Dispatchers.Default) {
        databaseRepository.tripQueries.selectById(id)
            .executeAsOneOrNull()
            ?.toTrip()
    }
    
    /**
     * Gets currently active trips.
     */
    fun getActiveTrips(): Flow<List<Trip>> {
        return databaseRepository.tripQueries.selectActive()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { dbTrips -> dbTrips.map { it.toTrip() } }
    }
    
    /**
     * Convert database Trip to domain Trip.
     */
    private fun DbTrip.toTrip(): Trip {
        return Trip(
            id = id,
            name = name,
            destination = destination,
            startDate = LocalDate.parse(startDate),
            endDate = LocalDate.parse(endDate),
            budget = budget,
            budgetCurrency = budgetCurrency,
            notes = notes
        )
    }
}