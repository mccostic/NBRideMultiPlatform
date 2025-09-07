package com.dovoh.android_mvi.core.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Home : Route
    @Serializable data object Register : Route
    // Example with args:
    @Serializable data class ProductDetails(val id: Long) : Route
}