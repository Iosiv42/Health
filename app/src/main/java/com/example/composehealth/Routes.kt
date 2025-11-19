package com.example.composehealth

import kotlinx.serialization.Serializable;

@Serializable
sealed class Routes {
    @Serializable
    data object Nutrition: Routes()

    @Serializable
    data object Sleep: Routes()

    @Serializable
    data object Activity: Routes()
}