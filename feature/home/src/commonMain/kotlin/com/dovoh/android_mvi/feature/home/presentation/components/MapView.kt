package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.components.map.MapLatLng
import com.dovoh.android_mvi.feature.home.presentation.components.map.MapMarker
import com.dovoh.android_mvi.feature.home.presentation.components.map.MarkerType
import com.dovoh.android_mvi.feature.home.presentation.components.map.NativeMapView

private const val SF_LAT = 37.7749
private const val SF_LNG = -122.4194
private const val DEFAULT_ZOOM = 13f

@Composable
fun MapView(
    state: RideState,
    modifier: Modifier = Modifier,
) {
    val markers = remember(state.origin, state.destination, state.screen, state.rideProgress) {
        buildList {
            // Origin marker
            state.origin?.let { origin ->
                if (origin.lat != 0.0) {
                    add(
                        MapMarker(
                            lat = origin.lat,
                            lng = origin.lng,
                            title = origin.name,
                            type = MarkerType.ORIGIN,
                        )
                    )
                } else {
                    // Default to SF center for "Current Location"
                    add(
                        MapMarker(
                            lat = SF_LAT,
                            lng = SF_LNG,
                            title = "You",
                            type = MarkerType.ORIGIN,
                        )
                    )
                }
            }

            // Destination marker
            state.destination?.let { dest ->
                add(
                    MapMarker(
                        lat = dest.lat,
                        lng = dest.lng,
                        title = dest.name,
                        type = MarkerType.DESTINATION,
                    )
                )
            }

            // Nearby cars on home screen
            if (state.screen == RideScreen.Home) {
                val nearbyCars = listOf(
                    37.7780 to -122.4150,
                    37.7720 to -122.4230,
                    37.7800 to -122.4100,
                    37.7690 to -122.4180,
                )
                nearbyCars.forEachIndexed { i, (lat, lng) ->
                    add(MapMarker(lat, lng, "Car ${i + 1}", MarkerType.CAR))
                }
            }

            // Driver position during ride
            if (state.screen == RideScreen.InRide && state.driver != null) {
                val originLat = state.origin?.lat?.takeIf { it != 0.0 } ?: SF_LAT
                val originLng = state.origin?.lng?.takeIf { it != 0.0 } ?: SF_LNG
                val destLat = state.destination?.lat ?: SF_LAT
                val destLng = state.destination?.lng ?: SF_LNG
                val fraction = state.rideProgress / 100f
                val driverLat = originLat + (destLat - originLat) * fraction
                val driverLng = originLng + (destLng - originLng) * fraction
                add(MapMarker(driverLat, driverLng, "Driver", MarkerType.DRIVER))
            }
        }
    }

    val routePoints = remember(state.origin, state.destination, state.screen) {
        val showRoute = state.screen in listOf(
            RideScreen.RideOptions,
            RideScreen.DriverFound,
            RideScreen.InRide,
        )
        if (showRoute && state.destination != null) {
            val originLat = state.origin?.lat?.takeIf { it != 0.0 } ?: SF_LAT
            val originLng = state.origin?.lng?.takeIf { it != 0.0 } ?: SF_LNG
            val destLat = state.destination.lat
            val destLng = state.destination.lng
            // Simple straight-line route with a mid-point offset for curve feel
            val midLat = (originLat + destLat) / 2 + 0.003
            val midLng = (originLng + destLng) / 2 - 0.005
            listOf(
                MapLatLng(originLat, originLng),
                MapLatLng(midLat, midLng),
                MapLatLng(destLat, destLng),
            )
        } else {
            null
        }
    }

    NativeMapView(
        modifier = modifier.fillMaxSize(),
        cameraLat = SF_LAT,
        cameraLng = SF_LNG,
        cameraZoom = DEFAULT_ZOOM,
        markers = markers,
        routePoints = routePoints,
        darkMode = true,
    )
}
