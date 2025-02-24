package com.joyplus.travelapp.android.ui.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.joyplus.travelapp.android.R
import com.joyplus.travelapp.model.Trip
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

/**
 * Dialog form for adding or editing a trip.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripForm(
    tripToEdit: Trip? = null,
    onSave: (Trip) -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = tripToEdit != null
    
    // Form state
    var name by remember { mutableStateOf(tripToEdit?.name ?: "") }
    var destination by remember { mutableStateOf(tripToEdit?.destination ?: "") }
    var startDate by remember { mutableStateOf(tripToEdit?.startDate ?: LocalDate.parse(java.time.LocalDate.now().toString())) }
    var endDate by remember { mutableStateOf(tripToEdit?.endDate ?: LocalDate.parse(java.time.LocalDate.now().plusDays(7).toString())) }
    var budget by remember { mutableStateOf(tripToEdit?.budget?.toString() ?: "") }
    var budgetCurrency by remember { mutableStateOf(tripToEdit?.budgetCurrency ?: "USD") }
    var notes by remember { mutableStateOf(tripToEdit?.notes ?: "") }
    
    // Dropdown state
    var showCurrencyDropdown by remember { mutableStateOf(false) }
    
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
                    text = if (isEditing) stringResource(R.string.edit_trip) else stringResource(R.string.create_trip),
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Trip name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.trip_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Destination field
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text(stringResource(R.string.trip_destination)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Start date field (simplified for now)
                OutlinedTextField(
                    value = startDate.toString(),
                    onValueChange = { 
                        try {
                            startDate = it.toLocalDate()
                        } catch (e: Exception) {
                            // Handle invalid date
                        }
                    },
                    label = { Text(stringResource(R.string.start_date)) },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Select start date"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // End date field (simplified for now)
                OutlinedTextField(
                    value = endDate.toString(),
                    onValueChange = { 
                        try {
                            endDate = it.toLocalDate()
                        } catch (e: Exception) {
                            // Handle invalid date
                        }
                    },
                    label = { Text(stringResource(R.string.end_date)) },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Select end date"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Budget fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = budget,
                        onValueChange = { budget = it },
                        label = { Text(stringResource(R.string.trip_budget)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(0.6f)
                    )
                    
                    androidx.compose.foundation.layout.Box(modifier = Modifier.weight(0.4f)) {
                        OutlinedTextField(
                            value = budgetCurrency,
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
                                        budgetCurrency = currencyOption
                                        showCurrencyDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Notes field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.trip_notes)) },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                
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
                            // Validate and save trip
                            val budgetValue = budget.toDoubleOrNull()
                            
                            val trip = Trip(
                                id = tripToEdit?.id ?: 0,
                                name = name,
                                destination = destination,
                                startDate = startDate,
                                endDate = endDate,
                                budget = budgetValue,
                                budgetCurrency = if (budgetValue != null) budgetCurrency else null,
                                notes = notes.ifEmpty { null }
                            )
                            
                            onSave(trip)
                        },
                        enabled = name.isNotEmpty() && destination.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}