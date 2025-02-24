package com.joyplus.travelapp.android.ui.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.joyplus.travelapp.android.ui.expenses.formatCurrency
import com.joyplus.travelapp.model.Trip
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Card item displaying a trip entry.
 */
@Composable
fun TripItem(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Trip name
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                // More options icon (for future implementation)
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Destination
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Destination",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = trip.destination,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Date range
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date range",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "${formatDate(trip.startDate)} - ${formatDate(trip.endDate)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Trip status
            val tripStatus = getTripStatus(trip)
            Text(
                text = tripStatus.statusText,
                style = MaterialTheme.typography.labelMedium,
                color = tripStatus.color
            )
            
            // Budget info (if available)
            if (trip.budget != null && trip.budgetCurrency != null) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Budget: ${formatCurrency(trip.budget, trip.budgetCurrency!!)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // TODO: Replace with actual budget usage when expenses are implemented
                val budgetUsage = 0.35f // Placeholder
                LinearProgressIndicator(
                    progress = budgetUsage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    }
}

/**
 * Format date for display.
 */
@Composable
fun formatDate(date: LocalDate): String {
    // Format: "15 Jan 2025"
    val formatted = "${date.dayOfMonth} ${getMonthShortName(date.monthNumber)} ${date.year}"
    return formatted
}

/**
 * Get short month name from month number.
 */
fun getMonthShortName(monthNumber: Int): String {
    return when (monthNumber) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
}

/**
 * Data class for trip status display information.
 */
data class TripStatus(
    val statusText: String,
    val color: androidx.compose.ui.graphics.Color
)

/**
 * Determine trip status (upcoming, active, or past).
 */
fun getTripStatus(trip: Trip): TripStatus {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    
    return when {
        today < trip.startDate -> TripStatus(
            statusText = "Upcoming (in ${countDays(today, trip.startDate)} days)",
            color = MaterialTheme.colorScheme.tertiary
        )
        today > trip.endDate -> TripStatus(
            statusText = "Past (${countDays(trip.endDate, today)} days ago)",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        else -> TripStatus(
            statusText = "Active (${countDays(trip.startDate, today) + 1} of ${
                countDays(trip.startDate, trip.endDate) + 1
            } days)",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Count days between two dates.
 */
fun countDays(from: LocalDate, to: LocalDate): Int {
    var current = from
    var days = 0
    
    while (current < to) {
        current = current.plus(1, DateTimeUnit.DAY)
        days++
    }
    
    return days
}