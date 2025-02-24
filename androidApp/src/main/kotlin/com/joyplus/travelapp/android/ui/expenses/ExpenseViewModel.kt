package com.joyplus.travelapp.android.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyplus.travelapp.model.Expense
import com.joyplus.travelapp.model.ExpenseCategory
import com.joyplus.travelapp.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * ViewModel for the Expenses screen.
 */
class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _selectedTripId = MutableStateFlow<Long?>(null)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAllExpenses()
    }

    /**
     * Loads all expenses from the repository.
     */
    fun loadAllExpenses() {
        _isLoading.value = true
        _selectedTripId.value = null
        
        repository.getAllExpenses()
            .onEach { 
                _expenses.value = it
                _isLoading.value = false
            }
            .catch {
                _error.value = it.message
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    /**
     * Loads expenses for a specific trip.
     */
    fun loadExpensesForTrip(tripId: Long) {
        _isLoading.value = true
        _selectedTripId.value = tripId
        
        repository.getExpensesByTrip(tripId)
            .onEach { 
                _expenses.value = it
                _isLoading.value = false
            }
            .catch {
                _error.value = it.message
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    /**
     * Loads expenses for a specific category.
     */
    fun loadExpensesByCategory(category: ExpenseCategory) {
        _isLoading.value = true
        
        repository.getExpensesByCategory(category)
            .onEach { 
                _expenses.value = it
                _isLoading.value = false
            }
            .catch {
                _error.value = it.message
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    /**
     * Adds a new expense.
     */
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.createExpense(expense)
                // Refresh the expense list depending on the current filter
                if (_selectedTripId.value != null) {
                    loadExpensesForTrip(_selectedTripId.value!!)
                } else {
                    loadAllExpenses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Updates an existing expense.
     */
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.updateExpense(expense)
                // Refresh the expense list depending on the current filter
                if (_selectedTripId.value != null) {
                    loadExpensesForTrip(_selectedTripId.value!!)
                } else {
                    loadAllExpenses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Deletes an expense.
     */
    fun deleteExpense(expenseId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expenseId)
                // Refresh the expense list depending on the current filter
                if (_selectedTripId.value != null) {
                    loadExpensesForTrip(_selectedTripId.value!!)
                } else {
                    loadAllExpenses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Clears any error state.
     */
    fun clearError() {
        _error.value = null
    }
}