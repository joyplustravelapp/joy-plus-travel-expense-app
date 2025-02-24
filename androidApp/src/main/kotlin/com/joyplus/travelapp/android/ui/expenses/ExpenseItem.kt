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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Card item displaying an expense entry.
 */
@Composable
fun ExpenseItem(
    expense: Expense,
    trip: Trip?,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon with background
            val categoryInfo = getCategoryInfo(expense.category)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(categoryInfo.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryInfo.icon,
                    contentDescription = expense.category.displayName(),
                    tint = categoryInfo.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Expense details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    trip?.let {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    expense.location?.let {
                        Text(
                            text = "• $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = expense.date.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Amount
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatCurrency(expense.amount, expense.currency),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (expense.isReimbursable) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Reimbursable",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // More options icon (for future implementation)
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Format currency amount with proper symbol.
 */
fun formatCurrency(amount: Double, currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(currencyCode)
    return format.format(amount)
}

/**
 * Data class holding category display information.
 */
data class CategoryInfo(
    val icon: ImageVector,
    val color: Color
)

/**
 * Get visual information for expense categories.
 */
fun getCategoryInfo(category: ExpenseCategory): CategoryInfo {
    return when (category) {
        ExpenseCategory.ACCOMMODATION -> CategoryInfo(
            icon = Icons.Default.Bed,
            color = accommodation
        )
        ExpenseCategory.FOOD -> CategoryInfo(
            icon = Icons.Default.LocalDining,
            color = food
        )
        ExpenseCategory.TRANSPORTATION -> CategoryInfo(
            icon = Icons.Default.DirectionsCar,
            color = transportation
        )
        ExpenseCategory.ACTIVITIES -> CategoryInfo(
            icon = Icons.Default.SportsHandball,
            color = activities
        )
        ExpenseCategory.SHOPPING -> CategoryInfo(
            icon = Icons.Default.ShoppingBag,
            color = shopping
        )
        ExpenseCategory.FEES -> CategoryInfo(
            icon = Icons.Default.Paid,
            color = fees
        )
        ExpenseCategory.MISCELLANEOUS -> CategoryInfo(
            icon = Icons.Default.CardTravel,
            color = miscellaneous
        )
    }
}