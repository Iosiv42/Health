package com.example.composehealth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Питание",
            icon = Icons.Filled.Face,
            route = Routes.Nutrition
        ),
        BottomNavItem(
            label = "Сон",
            iconResId = R.drawable.bedtime_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
            route = Routes.Sleep
        ),
        BottomNavItem(
            label = "Профиль",
            icon = Icons.Filled.Person,
            route = Routes.Profile
        )
    )
}