package com.joyplus.travelapp.android

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a bottom navigation item.
 */
data class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)