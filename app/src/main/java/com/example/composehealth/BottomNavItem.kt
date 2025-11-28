package com.example.composehealth

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    // Text below icon
    val label: String,
    // Icon
    val icon: ImageVector? = null,
    val iconResId: Int? = null,
    // Route to the specific screen
    val route: Routes,
)