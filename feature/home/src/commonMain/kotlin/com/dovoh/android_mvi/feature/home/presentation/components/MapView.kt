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
    Box(modifier = modifier.fillMaxSize()) {
        // Native map tiles background
        NativeMapView(
            modifier = Modifier.fillMaxSize(),
            cameraLat = SF_LAT,
            cameraLng = SF_LNG,
            cameraZoom = DEFAULT_ZOOM,
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
                // SelectDestination, RideComplete - no map overlay needed
            }
        }
    }
}
