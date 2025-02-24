package com.joyplus.travelapp.android.ui.trips

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
import com.joyplus.travelapp.model.Trip
import com.joyplus.travelapp.repository.TripRepository
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

/**
 * Main screen for displaying and managing trips.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    navController: NavController,
    tripRepository: TripRepository = get()
) {
    val coroutineScope = rememberCoroutineScope()
    var showAddTripDialog by remember { mutableStateOf(false) }
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }
    
    val trips by tripRepository.getAllTrips().collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tab_trips)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTripDialog = true },
                content = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.create_trip)) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (trips.isEmpty()) {
                // Show empty state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Trips Yet",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add your first trip",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Show trip list
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(trips) { trip ->
                        TripItem(
                            trip = trip,
                            onClick = { selectedTrip = trip }
                        )
                    }
                }
            }
        }
    }
    
    // Add Trip Dialog
    if (showAddTripDialog) {
        TripForm(
            onSave = { trip ->
                coroutineScope.launch {
                    tripRepository.createTrip(trip)
                    showAddTripDialog = false
                }
            },
            onDismiss = { showAddTripDialog = false }
        )
    }
    
    // Edit Trip Dialog
    if (selectedTrip != null) {
        TripForm(
            tripToEdit = selectedTrip,
            onSave = { trip ->
                coroutineScope.launch {
                    tripRepository.updateTrip(trip)
                    selectedTrip = null
                }
            },
            onDismiss = { selectedTrip = null }
        )
    }
}