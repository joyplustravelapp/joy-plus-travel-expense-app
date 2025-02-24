package com.joyplus.travelapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joyplus.travelapp.android.ui.expenses.ExpensesScreen
import com.joyplus.travelapp.android.ui.settings.SettingsScreen
import com.joyplus.travelapp.android.ui.stats.StatsScreen
import com.joyplus.travelapp.android.ui.theme.JoyPlusTravelTheme
import com.joyplus.travelapp.android.ui.trips.TripsScreen

/**
 * Main activity for the Android app that hosts the Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyPlusTravelTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    JoyPlusTravelApp()
                }
            }
        }
    }
}

/**
 * Main composable that sets up the app navigation.
 */
@Composable
fun JoyPlusTravelApp() {
    val navController = rememberNavController()
    
    // Define bottom navigation items
    val items = listOf(
        NavigationItem(
            route = "trips",
            title = stringResource(R.string.tab_trips),
            icon = Icons.Default.TravelExplore
        ),
        NavigationItem(
            route = "expenses",
            title = stringResource(R.string.tab_expenses),
            icon = Icons.Default.ShoppingCart
        ),
        NavigationItem(
            route = "stats",
            title = stringResource(R.string.tab_stats),
            icon = Icons.Default.BarChart
        ),
        NavigationItem(
            route = "settings",
            title = stringResource(R.string.tab_settings),
            icon = Icons.Default.Settings
        )
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "trips",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("trips") {
                TripsScreen(navController)
            }
            composable("expenses") {
                ExpensesScreen(navController)
            }
            composable("stats") {
                StatsScreen(navController)
            }
            composable("settings") {
                SettingsScreen(navController)
            }
        }
    }
}