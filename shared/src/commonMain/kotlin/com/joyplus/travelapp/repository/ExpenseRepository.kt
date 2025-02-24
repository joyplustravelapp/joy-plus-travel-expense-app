package com.joyplus.travelapp.repository

import com.joyplus.travelapp.db.Expense as DbExpense
import com.joyplus.travelapp.model.Expense
import com.joyplus.travelapp.model.ExpenseCategory
import com.joyplus.travelapp.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for managing Expense data in the database.
 */
class ExpenseRepository(private val databaseRepository: DatabaseRepository) {
    
    /**
     * Creates a new expense in the database.
     */
    suspend fun createExpense(expense: Expense): Long = withContext(Dispatchers.Default) {
        databaseRepository.expenseQueries.transactionWithResult {
            databaseRepository.expenseQueries.insertExpense(
                amount = expense.amount,
                currency = expense.currency,
                category = expense.category.name,
                description = expense.description,
                date = expense.date.toString(),
                tripId = expense.tripId,
                isReimbursable = if (expense.isReimbursable) 1L else 0L,
                receiptPath = expense.receiptPath,
                paymentMethod = expense.paymentMethod,
                location = expense.location
            )
            
            // Return the last inserted ID
            return@transactionWithResult databaseRepository.expenseQueries.selectAll().executeAsList().last().id
        }
    }
    
    /**
     * Updates an existing expense in the database.
     */
    suspend fun updateExpense(expense: Expense) = withContext(Dispatchers.Default) {
        databaseRepository.expenseQueries.updateExpense(
            amount = expense.amount,
            currency = expense.currency,
            category = expense.category.name,
            description = expense.description,
            date = expense.date.toString(),
            tripId = expense.tripId,
            isReimbursable = if (expense.isReimbursable) 1L else 0L,
            receiptPath = expense.receiptPath,
            paymentMethod = expense.paymentMethod,
            location = expense.location,
            id = expense.id
        )
    }
    
    /**
     * Deletes an expense from the database.
     */
    suspend fun deleteExpense(id: Long) = withContext(Dispatchers.Default) {
        databaseRepository.expenseQueries.deleteExpense(id)
    }
    
    /**
     * Gets all expenses as a Flow, which will emit new values when the database changes.
     */
    fun getAllExpenses(): Flow<List<Expense>> {
        return databaseRepository.expenseQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { dbExpenses -> dbExpenses.map { it.toExpense() } }
    }
    
    /**
     * Gets an expense by ID.
     */
    suspend fun getExpenseById(id: Long): Expense? = withContext(Dispatchers.Default) {
        databaseRepository.expenseQueries.selectById(id)
            .executeAsOneOrNull()
            ?.toExpense()
    }
    
    /**
     * Gets all expenses for a specific trip.
     */
    fun getExpensesByTrip(tripId: Long): Flow<List<Expense>> {
        return databaseRepository.expenseQueries.selectByTripId(tripId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { dbExpenses -> dbExpenses.map { it.toExpense() } }
    }
    
    /**
     * Gets all expenses with a specific category.
     */
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return databaseRepository.expenseQueries.selectByCategory(category.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { dbExpenses -> dbExpenses.map { it.toExpense() } }
    }
    
    /**
     * Gets expenses within a specific date range.
     */
    fun getExpensesByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Expense>> {
        return databaseRepository.expenseQueries.selectByDateRange(startDate.toString(), endDate.toString())
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { dbExpenses -> dbExpenses.map { it.toExpense() } }
    }
    
    /**
     * Gets the total expenses for a trip in a specific currency.
     */
    suspend fun getTripTotal(tripId: Long, currency: String): Double = withContext(Dispatchers.Default) {
        databaseRepository.expenseQueries.getTotalByTrip(tripId, currency)
            .executeAsOneOrNull()
            ?.total ?: 0.0
    }
    
    /**
     * Gets expense category totals for a trip in a specific currency.
     */
    suspend fun getTripCategoryTotals(tripId: Long, currency: String): ExpenseSummary = withContext(Dispatchers.Default) {
        val totals = databaseRepository.expenseQueries.getCategoryTotalByTrip(tripId, currency)
            .executeAsList()
        
        val categoryMap = totals.associate { 
            ExpenseCategory.fromString(it.category) to (it.total ?: 0.0)
        }
        
        val totalAmount = categoryMap.values.sum()
        
        return@withContext ExpenseSummary(
            totalAmount = totalAmount,
            currency = currency,
            categoryTotals = categoryMap
        )
    }
    
    /**
     * Convert database Expense to domain Expense.
     */
    private fun DbExpense.toExpense(): Expense {
        return Expense(
            id = id,
            amount = amount,
            currency = currency,
            category = ExpenseCategory.fromString(category),
            description = description,
            date = LocalDate.parse(date),
            tripId = tripId,
            isReimbursable = isReimbursable == 1L,
            receiptPath = receiptPath,
            paymentMethod = paymentMethod,
            location = location
        )
    }
}