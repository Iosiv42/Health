package com.example.composehealth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Nutrition",
            icon = Icons.Filled.Face,
            route = Routes.Nutrition
        ),
        BottomNavItem(
            label = "Sleep",
            icon = Icons.Filled.Search,
            route = Routes.Sleep
        ),
        BottomNavItem(
            label = "Activity",
            icon = Icons.Filled.Person,
            route = Routes.Activity
        )
    )
}