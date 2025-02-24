package com.joyplus.travelapp.android.ui.expenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joyplus.travelapp.android.R
import com.joyplus.travelapp.model.Expense
import com.joyplus.travelapp.repository.ExpenseRepository
import com.joyplus.travelapp.repository.TripRepository
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

/**
 * Screen for displaying and managing expenses.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navController: NavController,
    expenseRepository: ExpenseRepository = get(),
    tripRepository: TripRepository = get()
) {
    val coroutineScope = rememberCoroutineScope()
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    
    val expenses by expenseRepository.getAllExpenses().collectAsState(initial = emptyList())
    val trips by tripRepository.getAllTrips().collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tab_expenses)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExpenseDialog = true },
                content = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_expense)) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (expenses.isEmpty()) {
                // Show empty state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Expenses Yet",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add your first expense",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Show expense list
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(expenses) { expense ->
                        ExpenseItem(
                            expense = expense,
                            trip = trips.find { it.id == expense.tripId },
                            onClick = { selectedExpense = expense }
                        )
                    }
                }
            }
        }
    }
    
    // Add/Edit Expense Dialog
    if (showAddExpenseDialog) {
        ExpenseForm(
            availableTrips = trips,
            onSave = { expense ->
                coroutineScope.launch {
                    expenseRepository.createExpense(expense)
                    showAddExpenseDialog = false
                }
            },
            onDismiss = { showAddExpenseDialog = false }
        )
    }
    
    // Edit Expense Dialog
    if (selectedExpense != null) {
        ExpenseForm(
            expenseToEdit = selectedExpense,
            availableTrips = trips,
            onSave = { expense ->
                coroutineScope.launch {
                    expenseRepository.updateExpense(expense)
                    selectedExpense = null
                }
            },
            onDismiss = { selectedExpense = null }
        )
    }
}