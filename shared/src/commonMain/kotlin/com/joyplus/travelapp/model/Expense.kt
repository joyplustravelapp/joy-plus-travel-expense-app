package com.joyplus.travelapp.model

import kotlinx.datetime.LocalDate

/**
 * Data class representing an expense entry in the app.
 */
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val currency: String,
    val category: ExpenseCategory,
    val description: String,
    val date: LocalDate,
    val tripId: Long,
    val isReimbursable: Boolean = false,
    val receiptPath: String? = null,
    val paymentMethod: String? = null,
    val location: String? = null
)

/**
 * Enum representing different expense categories.
 */
enum class ExpenseCategory {
    ACCOMMODATION,
    FOOD,
    TRANSPORTATION,
    ACTIVITIES,
    SHOPPING,
    FEES,
    MISCELLANEOUS;
    
    fun displayName(): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }
    
    companion object {
        fun fromString(value: String): ExpenseCategory {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                MISCELLANEOUS
            }
        }
    }
}

/**
 * Data class representing a trip, which contains multiple expenses.
 */
data class Trip(
    val id: Long = 0,
    val name: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val budget: Double? = null,
    val budgetCurrency: String? = null,
    val notes: String? = null
)

/**
 * Data class for grouping expenses by category for analysis.
 */
data class ExpenseSummary(
    val totalAmount: Double,
    val currency: String,
    val categoryTotals: Map<ExpenseCategory, Double>
)