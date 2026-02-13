package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.components.map.AnimatedCarsOverlay
import com.dovoh.android_mvi.feature.home.presentation.components.map.NativeMapView
import com.dovoh.android_mvi.feature.home.presentation.components.map.RouteOverlay

private const val SF_LAT = 37.7749
private const val SF_LNG = -122.4194
private const val DEFAULT_ZOOM = 13f

@Composable
fun MapView(
    state: RideState,
    modifier: Modifier = Modifier,
) {
    // Camera positioning based on state
    val originLat = state.origin?.lat ?: SF_LAT
    val originLng = state.origin?.lng ?: SF_LNG
    val destLat = state.destination?.lat ?: SF_LAT
    val destLng = state.destination?.lng ?: SF_LNG

    val hasRoute = state.destination != null && state.screen in setOf(
        RideScreen.RideOptions,
        RideScreen.Searching,
        RideScreen.DriverFound,
        RideScreen.InRide,
    )

    val cameraLat = if (hasRoute) (originLat + destLat) / 2.0 else SF_LAT
    val cameraLng = if (hasRoute) (originLng + destLng) / 2.0 else SF_LNG
    val cameraZoom = if (hasRoute) 12.5f else DEFAULT_ZOOM

    Box(modifier = modifier.fillMaxSize()) {
        // Native map tiles background
        NativeMapView(
            modifier = Modifier.fillMaxSize(),
            cameraLat = cameraLat,
            cameraLng = cameraLng,
            cameraZoom = cameraZoom,
            darkMode = true,
        )

        // Compose animation overlays
        when (state.screen) {
            RideScreen.Home -> {
                AnimatedCarsOverlay(modifier = Modifier.fillMaxSize())
            }

            RideScreen.RideOptions,
            RideScreen.Searching,
            RideScreen.DriverFound,
            -> {
                RouteOverlay(
                    rideProgress = 0f,
                    showDriver = false,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            RideScreen.InRide -> {
                RouteOverlay(
                    rideProgress = state.rideProgress,
                    showDriver = true,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                // SelectDestination, MapPicker, RideComplete - no map overlay needed
            }
        }
    }
}
