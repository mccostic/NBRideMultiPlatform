package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class MapMarker(
    val lat: Double,
    val lng: Double,
    val title: String,
    val type: MarkerType,
)

enum class MarkerType { ORIGIN, DESTINATION, CAR, DRIVER }

data class MapLatLng(val lat: Double, val lng: Double)

@Composable
expect fun NativeMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    markers: List<MapMarker>,
    routePoints: List<MapLatLng>?,
    darkMode: Boolean,
)
