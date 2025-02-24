package com.joyplus.travelapp.android.ui.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.joyplus.travelapp.android.R
import com.joyplus.travelapp.android.ui.theme.accommodation
import com.joyplus.travelapp.android.ui.theme.activities
import com.joyplus.travelapp.android.ui.theme.fees
import com.joyplus.travelapp.android.ui.theme.food
import com.joyplus.travelapp.android.ui.theme.miscellaneous
import com.joyplus.travelapp.android.ui.theme.shopping
import com.joyplus.travelapp.android.ui.theme.transportation
import com.joyplus.travelapp.model.Expense
import com.joyplus.travelapp.model.ExpenseCategory
import com.joyplus.travelapp.model.Trip
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

/**
 * Dialog form for adding or editing an expense.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseForm(
    expenseToEdit: Expense? = null,
    availableTrips: List<Trip> = emptyList(),
    onSave: (Expense) -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = expenseToEdit != null
    
    // Form state
    var amount by remember { mutableStateOf(expenseToEdit?.amount?.toString() ?: "") }
    var currency by remember { mutableStateOf(expenseToEdit?.currency ?: "USD") }
    var selectedCategory by remember { mutableStateOf(expenseToEdit?.category ?: ExpenseCategory.MISCELLANEOUS) }
    var description by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var date by remember { mutableStateOf(expenseToEdit?.date ?: LocalDate.parse(java.time.LocalDate.now().toString())) }
    var selectedTrip by remember { mutableStateOf(expenseToEdit?.tripId ?: availableTrips.firstOrNull()?.id ?: 0L) }
    var isReimbursable by remember { mutableStateOf(expenseToEdit?.isReimbursable ?: false) }
    var paymentMethod by remember { mutableStateOf(expenseToEdit?.paymentMethod ?: "") }
    var location by remember { mutableStateOf(expenseToEdit?.location ?: "") }
    
    // Dropdown states
    var showCurrencyDropdown by remember { mutableStateOf(false) }
    var showTripDropdown by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (isEditing) stringResource(R.string.edit_expense) else stringResource(R.string.add_expense),
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Amount and Currency fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(stringResource(R.string.expense_amount)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(0.6f)
                    )
                    
                    Box(modifier = Modifier.weight(0.4f)) {
                        OutlinedTextField(
                            value = currency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.expense_currency)) },
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Select currency"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCurrencyDropdown = true }
                        )
                        
                        DropdownMenu(
                            expanded = showCurrencyDropdown,
                            onDismissRequest = { showCurrencyDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.4f)
                        ) {
                            listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD").forEach { currencyOption ->
                                DropdownMenuItem(
                                    text = { Text(currencyOption) },
                                    onClick = {
                                        currency = currencyOption
                                        showCurrencyDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Category picker
                Text(
                    text = stringResource(R.string.expense_category),
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ExpenseCategory.values().forEach { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.expense_description)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Date field (simplified for now)
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = { 
                        try {
                            date = it.toLocalDate()
                        } catch (e: Exception) {
                            // Handle invalid date
                        }
                    },
                    label = { Text(stringResource(R.string.expense_date)) },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Trip selection (only show if there are trips available)
                if (availableTrips.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = availableTrips.find { it.id == selectedTrip }?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.trip_name)) },
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Select trip"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTripDropdown = true }
                        )
                        
                        DropdownMenu(
                            expanded = showTripDropdown,
                            onDismissRequest = { showTripDropdown = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableTrips.forEach { trip ->
                                DropdownMenuItem(
                                    text = { Text(trip.name) },
                                    onClick = {
                                        selectedTrip = trip.id
                                        showTripDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Location field
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(stringResource(R.string.expense_location)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Payment method field
                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text(stringResource(R.string.expense_payment_method)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Reimbursable checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isReimbursable,
                        onCheckedChange = { isReimbursable = it }
                    )
                    Text(
                        text = stringResource(R.string.expense_reimbursable),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    
                    Button(
                        onClick = {
                            // Validate and save expense
                            val amountValue = amount.toDoubleOrNull() ?: 0.0
                            
                            val expense = Expense(
                                id = expenseToEdit?.id ?: 0,
                                amount = amountValue,
                                currency = currency,
                                category = selectedCategory,
                                description = description,
                                date = date,
                                tripId = selectedTrip,
                                isReimbursable = isReimbursable,
                                paymentMethod = paymentMethod.ifEmpty { null },
                                location = location.ifEmpty { null }
                            )
                            
                            onSave(expense)
                        },
                        enabled = amount.isNotEmpty() && description.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

/**
 * Category selection item with icon and color.
 */
@Composable
fun CategoryItem(
    category: ExpenseCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = when (category) {
        ExpenseCategory.ACCOMMODATION -> accommodation
        ExpenseCategory.FOOD -> food
        ExpenseCategory.TRANSPORTATION -> transportation
        ExpenseCategory.ACTIVITIES -> activities
        ExpenseCategory.SHOPPING -> shopping
        ExpenseCategory.FEES -> fees
        ExpenseCategory.MISCELLANEOUS -> miscellaneous
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isSelected) color else color.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            // Here you would typically add an icon for each category
        }
        Text(
            text = category.displayName(),
            style = MaterialTheme.typography.labelSmall
        )
    }
}