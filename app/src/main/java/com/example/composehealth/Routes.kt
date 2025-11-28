package com.example.composehealth

import kotlinx.serialization.Serializable;
import java.time.LocalDate

@Serializable
sealed class Routes {
    @Serializable
    data object Nutrition: Routes()

    @Serializable
    data object Sleep: Routes()

    @Serializable
    data object Profile: Routes()

    @Serializable
    data object AddNewFood: Routes()

    @Serializable
    data class FoodSearch(val mealName: String): Routes()

    @Serializable
    data object AddActivityTrack: Routes()

    @Serializable
    data object SleepCalculator: Routes()
}