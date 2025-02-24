package com.joyplus.travelapp.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

/**
 * A card displaying expense information.
 */
@Composable
fun ExpenseItem(
    expense: Expense,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(getCategoryColor(expense.category).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(expense.category),
                    contentDescription = null,
                    tint = getCategoryColor(expense.category),
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
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = expense.date.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (expense.location != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = expense.location!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Amount
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${expense.amount.toString()} ${expense.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = expense.category.displayName(),
                    style = MaterialTheme.typography.bodySmall,
                    color = getCategoryColor(expense.category)
                )
            }
            
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Returns the corresponding icon for a given expense category.
 */
@Composable
fun getCategoryIcon(category: ExpenseCategory): ImageVector {
    return when (category) {
        ExpenseCategory.ACCOMMODATION -> Icons.Default.Apartment
        ExpenseCategory.FOOD -> Icons.Default.LocalDining
        ExpenseCategory.TRANSPORTATION -> Icons.Default.DirectionsCar
        ExpenseCategory.ACTIVITIES -> Icons.Default.SportsEsports
        ExpenseCategory.SHOPPING -> Icons.Default.ShoppingBag
        ExpenseCategory.FEES -> Icons.Default.MonetizationOn
        ExpenseCategory.MISCELLANEOUS -> Icons.Default.AccessTime
    }
}

/**
 * Returns the corresponding color for a given expense category.
 */
@Composable
fun getCategoryColor(category: ExpenseCategory): Color {
    return when (category) {
        ExpenseCategory.ACCOMMODATION -> accommodation
        ExpenseCategory.FOOD -> food
        ExpenseCategory.TRANSPORTATION -> transportation
        ExpenseCategory.ACTIVITIES -> activities
        ExpenseCategory.SHOPPING -> shopping
        ExpenseCategory.FEES -> fees
        ExpenseCategory.MISCELLANEOUS -> miscellaneous
    }
}